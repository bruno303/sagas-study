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

### Sync
- [x] Create order (same service that manage the saga)
- [x] Create ticket (call restaurant service)
- [x] Pay (call payment service)
- [x] Notify (call notification service)
- [ ] Finish order 

### Async
- [x] Create order (same service that manage the saga)
- [x] Create ticket (send message to restaurant service)
- [x] Listen ticket response
- [x] Pay (send message to payment service)
- [x] Listen payment response
- [x] Notify (send message to notification service)
- [x] Listen notification response
- [x] Finish order
