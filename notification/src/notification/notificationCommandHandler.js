import { publish } from './notificationEventPublisher.js'
import { v4 as uuidv4 } from "uuid";

class NotificationCommandHandler {
  constructor() {}

  handle = async function(message) {
    console.log(`Message received: ${message.Body}`)
    const body = JSON.parse(message.Body)
    const notification = {
      endToEndId: body.endToEndId,
      order: body.order,
      ticketId: body.ticketId,
      paymentId: body.paymentId,
      notificationId: uuidv4(),
      errors: null
    }

    const jsonToSend = JSON.stringify(notification)
    console.log(`Publishing message: ${jsonToSend}`)
    publish(jsonToSend)
  }
}

export { NotificationCommandHandler }
