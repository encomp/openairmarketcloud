# Open Air Market

### Description
The goal of the app is to develop a point of sale (POS) app. The point of sale allows a merchant to 
calculate the amount owed by a customer. The application prepares a ticket for the customer and 
indicates the available options for the customer to perform the payment. It is also the point at 
which a customer makes a payment to the merchant in exchange for goods. Upon receiving the payment, 
the merchant may issue a receipt for the transaction, which is printed or emailed to the customer.

### Intended User
The user based for the app are retail merchants that lack a POS. The goal of the system is to allow 
merchants to define the products and allow them to perform sale transactions. The system will allow 
merchants to have more than one terminal if needed since the POS backend is cloud base.

### Features
- Sign In/Create Account Screen
- Point of Sale Screen
- Support up to 3 Sales (receipts) at the same time
- Allow add quick access product to a specific ticket
- Perform a sale from a given receipt
- Widget that provide the dollar amount of the intraday sales

### User Interface Mocks
#### Screen Create Account/Log In

The following screen allows to create a new account to allo a new user to gain access to the POS 
application. However, only after the email verifciation occurred a user will be able to Log In to 
the application.

| Create Account | Log In | 
| -------------- |:------:|
| <img src="/md/create_account.gif" width="400" height="800"> | <img src="/md/signIn.gif" width="400" height="800"> |

#### Screen Pos Mobile

The following screen allows the user to perform a sale. The user could perform 3 different sales 
simultaneously by having 3 receipt tabs. Each tab receipt provides all the different products with 
the correspondent quantities and price of each product. To capture the desired product to sell there 
is a search in the bottom app bar. This icon will allow to enter a product by using the barcode of 
the specific product. And at the bottom it provides the total of amount of the sale. The entire 
receipt could be cancel (by clicking on the cancel button) or complete (by clicking on the pay 
button).

| Receipts | Cancel/Pay | 
| -------- |:----------:|
| <img src="/md/pos_screen.gif" width="400" height="800"> | <img src="/md/cancel_pay.gif" width="400" height="800"> |

#### Screen Pos Phone Mobile Quick Access

The following screen allows the user sell a product using the quick access bottom sheet. The 
merchant can select the product he desires to sell by using the bottom sheet that will display a 
grid of buttons with products that are more frequently bought or lack a codebar. If the merchant 
clicks on one of the product it the selected product will appear on the selected receipt as a new 
line.

| Quick Acces | Cancel/Pay | 
| ----------- |:----------:|
| <img src="/md/quick_access.gif" width="400" height="800"> | <img src="/md/pay_quick.gif" width="400" height="800"> |

#### Screen Widget 

The app will provide a widget that will provide the intraday dollar amount of the sales performed.

| Sales Widget |
| ------------ |
| <img src="/md/widget.gif" width="400" height="800"> | 