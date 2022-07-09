import { publish } from './notificationEventPublisher.js'
import { v4 as uuidv4 } from "uuid";
import { splitClient } from '../libs/splitio/split.js';
import { randomIntFromInterval } from '../libs/random/random.js'

class NotificationCommandHandler {
  constructor() {}

  handle = async (message) => {
    console.log(`Message received: ${message.Body}`)
    const body = JSON.parse(message.Body)
    var notification = null

    if (this.shouldReturnError()) {
      notification = {
        endToEndId: body.endToEndId,
        order: body.order,
        ticketId: body.ticketId,
        paymentId: body.paymentId,
        notificationId: null,
        errors: ['Error when sending notification']
      }
    } else {
      notification = {
        endToEndId: body.endToEndId,
        order: body.order,
        ticketId: body.ticketId,
        paymentId: body.paymentId,
        notificationId: uuidv4(),
        errors: null
      }
    }

    const jsonToSend = JSON.stringify(notification)
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

export { NotificationCommandHandler }
