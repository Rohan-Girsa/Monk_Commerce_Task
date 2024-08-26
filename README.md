COUPON MANAGEMENT API

This is a RESTful API for managing coupons and applying them to shopping carts. Built using Spring Boot gradle project, this API lets you create, update, and delete coupons, and apply them to carts.

Endpoints
1. Create a Coupon
    URL: /coupons
    Method: POST
    Request body example : 
      {
        "type": "cart-wise",
        "couponDetails": {
           "discount": 10,
           "threshold": 50
         },
        "expirationDate": "2024-12-31T23:59:59Z"
      }
    Success Response - status code : 200 and return coupon along with ID.
2.  Get All Coupons
      URL: /coupons
      Method: GET
      Success Response - status code: 200 and list of coupons.
3. Get a Coupon by ID
    URL: /coupons/{id}
    Method: GET
    URL Params:
    Required: id=[string]
    Success Response - status code 200 and coupon with id asmentioned in url
4. Update a Coupon
    URL: /coupons/{id}
    Method: PUT
    URL Params:
    Required: id=[string]
   Request body example : 
      {
        "type": "cart-wise",
        "couponDetails": {
           "discount": 70,
           "threshold": 1000
         },
        "expirationDate": "2024-12-31T23:59:59Z"
      }
    Success Response - status code : 200 and return coupon along with ID.
5. Delete a Coupon
   URL: /coupons/{id}
    Method: DELETE
    URL Params:
    Required: id=[string]
    Success Response - status code 200 and coupon with id as mentioned in url and message coupons deleted successfully
6. Get Applicable Coupons for Cart
     URL: /coupons/applicable-coupons
     Method: POST
     Request Body Example:
     {
        "cartItems": [
        {
            "productId": 1,
            "quantity": 2,
            "price": 10.0
          }
        ]
      }
   Success Response - status code : 200 and list of all applicable coupons.
7. Apply a Coupon to a Cart
     URL: /coupons/apply-coupon/{id}
     Method: POST
     URL Params:
     Required: id=[string]

Known Issues
  Apply Coupon Not Working
  Issue: The applyCoupon method sometimes doesn't apply discounts correctly.
  Possible Problems:
    The logic for "cart-wise" discounts might not be handling the total cart value and per-item discount distribution correctly.
    The "product-wise" discount might not be applied correctly if the item's price is equal to the threshold.
    The "Buy X Get Y" logic might not adjust the cart items as expected.
