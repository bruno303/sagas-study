import { publish } from './ticketCreatedEventPublisher.js'
import { v4 as uuidv4 } from "uuid";

class CreateTicketCommandHandler {
  constructor() {}

  handle = async function(message) {
    console.log(`Message received: ${message.Body}`)
    const body = JSON.parse(message.Body)
    const ticket = {
      endToEndId: body.endToEndId,
      orderId: body.orderId,
      ticket: {
        id: uuidv4(),
        name: "ticket",
        creationDateTime: new Date()
      },
      errors: null
    }

    const jsonToSend = JSON.stringify(ticket)
    console.log(`Publishing message: ${jsonToSend}`)
    publish(jsonToSend)
  }
}

export { CreateTicketCommandHandler }
