import 'dotenv/config'
import express, { json, urlencoded } from 'express'
import { ticketRouter } from './ticket/routes/ticketRouter.js';
import { consumer } from './ticket/createTicketListener.js';

const app = express()
const port = 3000

app.use(urlencoded({ extended: false }))
app.use(json())

app.use('/ticket', ticketRouter)

app.listen(port, () => {
  console.log(`Example app listening on port ${port}`)
})

consumer.start()
