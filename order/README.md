# Order - Saga Study

## Flow
* Create order with CREATED status
* Create a ticket in restaurant
* Pay
* Send notification

The `Pay` step is a pivot transaction in this saga.
If the payment is successfully, next steps will retry until they are successful as well.
If the payment has errors, the previous steps will be undone.


## Scope

### Async
- [x] Transactional Outbox table pattern
- [x] Message publisher strategy
- [x] Create order (same service that manage the saga)
- [x] Create ticket (send message to restaurant service)
- [x] Listen ticket response
- [x] Pay (send message to payment service)
- [x] Listen payment response
- [x] Notify (send message to notification service)
- [x] Listen notification response
- [x] Finish order
- [ ] Handle failure in all necessary steps
- [ ] Sent internal events for each order's status update
- [ ] Configure DLQ for notification command queue
- [ ] Docs
