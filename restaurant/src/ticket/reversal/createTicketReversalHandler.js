class CreateTicketReversalHandler {
  constructor() {}

  handle = async (message) => {
    console.log(`Message received for reversal: ${message.Body}`)
  }
}



export { CreateTicketReversalHandler }
