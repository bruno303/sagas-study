import { publish } from './ticketCreatedEventPublisher.js'
import { v4 as uuidv4 } from "uuid";
import { splitClient } from '../libs/splitio/split.js';
import { randomIntFromInterval } from '../libs/random/random.js'

class CreateTicketCommandHandler {
  constructor() {}

  handle = async (message) => {
    console.log(`Message received: ${message.Body}`)

    const body = JSON.parse(message.Body)
    var result = null

    if (this.shouldReturnError()) {
      result = {
        endToEndId: body.endToEndId,
        orderId: body.orderId,
        ticket: null,
        errors: ["Error when creating ticket"]
      }
    } else {
      result = {
        endToEndId: body.endToEndId,
        orderId: body.orderId,
        ticket: {
          id: uuidv4(),
          name: "ticket",
          creationDateTime: new Date()
        },
        errors: null
      }
    }

    const jsonToSend = JSON.stringify(result)
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



export { CreateTicketCommandHandler }
