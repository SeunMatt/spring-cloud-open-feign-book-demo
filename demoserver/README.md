# Demo Commerce
This is a very simple e-commerce API service. It exposes some endpoints that can be invoked by any API consumer

# APIs


| METHOD		|	API								|	Auth Method	
|-------	----	|-----------------------------------	|--------------
| GET		| /									| None		
| GET		| /basic/products					| HTTP Basic
| GET		| /basic/products/{productName}		| HTTP Basic
| POST		| /core/token/cart/items				| API Token
| POST		| /core/basic/refunds				| API Token