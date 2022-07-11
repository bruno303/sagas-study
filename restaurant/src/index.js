import 'dotenv/config'
import express, { json, urlencoded } from 'express'
import { ticketRouter } from './ticket/routes/ticketRouter.js';
import { consumer as createTicketListener } from './ticket/createTicketListener.js';
import { consumer as createTicketReversalListener } from './ticket/reversal/createTicketReversalListener.js'

const app = express()
const port = process.env['PORT'] || 3000

app.use(urlencoded({ extended: false }))
app.use(json())

app.use('/ticket', ticketRouter)

app.listen(port, () => {
  console.log(`Example app listening on port ${port}`)
})

createTicketListener.start()
createTicketReversalListener.start()
