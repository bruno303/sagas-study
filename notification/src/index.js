import 'dotenv/config'
import express, { json, urlencoded } from 'express'
import cookieParser from 'cookie-parser'
import logger from 'morgan'

import { router as notificationRouter } from './notification/routes/notificationRouter.js';
import { consumer } from './notification/notificationListener.js';

const app = express()
const port = process.env['PORT'] || 3000

app.use(logger('dev'))
app.use(json())
app.use(urlencoded({ extended: false }))
app.use(cookieParser())

app.use('/notification', notificationRouter)

app.listen(port, () => console.log(`Listening on port ${port}`))
consumer.start()
