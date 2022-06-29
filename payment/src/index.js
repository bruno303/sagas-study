import 'dotenv/config'
import express from 'express'
import cookieParser from 'cookie-parser'
import logger from 'morgan'

import { router as paymentRouter } from './payment/routes/paymentRouter.js'
import { consumer } from "./payment/payCommandListener.js";

const app = express();
const port = 3000

app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());

app.use('/payment', paymentRouter);

app.listen(port, () => console.log(`Listening on port ${port}`))
consumer.start()
