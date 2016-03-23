Project 5: 

For project 5, we extend project 4 to allow users to buy an item with credit card info. We enable SSL on tomcat and keep information through a sequence of pages by using http session. 


    Q1: For which communication(s) do you use the SSL encryption? If you are encrypting the communication from (1) to (2) in Figure 2, for example, write (1)→(2) in your answer.

We use SSL encryption for 4->5 and 5->6. 


    Q2: How do you ensure that the item was purchased exactly at the Buy_Price of that particular item?

We create a session at 1, and have a map that maps the item ID to name and price. From 1 to 6, when we want to retrieve for item buy price, we ask from the map, which is consistent throughout the session. 