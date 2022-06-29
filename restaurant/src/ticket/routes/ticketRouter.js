import { Router } from 'express';
import { v4 as uuidv4 } from "uuid";
const ticketRouter = Router()

ticketRouter.post('/', (req, res) => {
    res.send({
        id: uuidv4(),
        name: "name",
        creationDateTime: new Date()
    })
})

export { ticketRouter }
