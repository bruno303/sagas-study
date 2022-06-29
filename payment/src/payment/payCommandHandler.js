import { publish } from './paymentEventPublisher.js'
import { v4 as uuidv4 } from "uuid";

class PayCommandHandler {
  constructor() {}

  handle = async function(message) {
    console.log(`Message received: ${message.Body}`)
    const body = JSON.parse(message.Body)
    const payment = {
      endToEndId: body.endToEndId,
      orderId: body.orderId,
      ticketId: body.ticketId,
      payment: {
        id: uuidv4(),
        name: "ticket",
        amount: 10.00,
        paymentDateTime: new Date()
      },
      errors: null
    }

    const jsonToSend = JSON.stringify(payment)
    console.log(`Publishing message: ${jsonToSend}`)
    publish(jsonToSend)
  }
}

export { PayCommandHandler }
