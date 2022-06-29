import { Router } from 'express';
const router = Router();
import { v4 as uuidv4 } from 'uuid';

router.post('/', (req, res) => {
  res.send({
    id: uuidv4(),
    name: "name",
    paymentDateTime: new Date()
  })
});

export { router };
