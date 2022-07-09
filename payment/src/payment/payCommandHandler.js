import { publish } from './paymentEventPublisher.js'
import { v4 as uuidv4 } from "uuid";
import { splitClient } from '../libs/splitio/split.js';
import { randomIntFromInterval } from '../libs/random/random.js'

class PayCommandHandler {
  constructor() {}

  handle = async (message) => {
    console.log(`Message received: ${message.Body}`)
    const body = JSON.parse(message.Body)
    var payment = null

    if (this.shouldReturnError()) {
      payment = {
        endToEndId: body.endToEndId,
        orderId: body.orderId,
        ticketId: body.ticketId,
        payment: null,
        errors: ['Error during payment validation']
      }
    } else {
      payment = {
        endToEndId: body.endToEndId,
        orderId: body.orderId,
        ticketId: body.ticketId,
        payment: {
          id: uuidv4(),
          name: "payment",
          amount: 10.00,
          paymentDateTime: new Date()
        },
        errors: null
      }
    }

    const jsonToSend = JSON.stringify(payment)
    console.log(`Publishing message: ${jsonToSend}`)
    publish(jsonToSend)
  }

  shouldReturnError() {
    console.log('Evaluating split for error')
    const treatment = splitClient.getTreatment(randomIntFromInterval(1, 5).toString(), 'error_when_processing')
      if (treatment == 'on') {
        console.log('Split for error evaluated to true')
        return true
    }
    
    console.log('Split for error evaluated to false')
    return false
  }
}

export { PayCommandHandler }
