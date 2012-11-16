JAVAC = javac
#TPCW_HOME=/victim5/JavaWebServer2.0
TPCW_HOME=/usr/WebSphere/AppServer
SERVLET_HOME=$(TPCW_HOME)/servlets
CLASS_HOME=$(TPCW_HOME)/classes
#Need to set you class path to:
#CLASSPATH=/victim5/JavaWebServer2.0/classes:/home/cain/JSDK2.0/lib/jsdk.jar

all: 	$(CLASS_HOME)/Address.class \
	$(CLASS_HOME)/Book.class \
	$(CLASS_HOME)/BuyConfirmResult.class \
	$(CLASS_HOME)/Cart.class \
	$(CLASS_HOME)/CartLine.class \
	$(CLASS_HOME)/Customer.class \
	$(CLASS_HOME)/Order.class \
	$(CLASS_HOME)/OrderLine.class \
	$(CLASS_HOME)/ShortBook.class \
	$(CLASS_HOME)/TPCW_Database.class \
	$(CLASS_HOME)/TPCW_promotional_processing.class \
	$(CLASS_HOME)/TPCW_say_hello.class \
	$(CLASS_HOME)/TPCW_Util.class \
	$(SERVLET_HOME)/TPCW_admin_request_servlet.class \
	$(SERVLET_HOME)/TPCW_admin_response_servlet.class \
	$(SERVLET_HOME)/TPCW_best_sellers_servlet.class \
	$(SERVLET_HOME)/TPCW_buy_confirm_servlet.class \
	$(SERVLET_HOME)/TPCW_buy_request_servlet.class \
	$(SERVLET_HOME)/TPCW_customer_registration_servlet.class \
	$(SERVLET_HOME)/TPCW_execute_search.class \
	$(SERVLET_HOME)/TPCW_home_interaction.class \
	$(SERVLET_HOME)/TPCW_new_products_servlet.class \
	$(SERVLET_HOME)/TPCW_new_products_servlet.class \
	$(SERVLET_HOME)/TPCW_order_inquiry_servlet.class \
	$(SERVLET_HOME)/TPCW_order_display_servlet.class \
	$(SERVLET_HOME)/TPCW_product_detail_servlet.class \
	$(SERVLET_HOME)/TPCW_search_request_servlet.class \
	$(SERVLET_HOME)/TPCW_shopping_cart_interaction.class

#stand alone classes
$(CLASS_HOME)/Address.class: Address.java
	$(JAVAC) -d $(CLASS_HOME) Address.java

$(CLASS_HOME)/Book.class: Book.java
	$(JAVAC) -d $(CLASS_HOME) Book.java

$(CLASS_HOME)/BuyConfirmResult.class: BuyConfirmResult.java
	$(JAVAC) -d $(CLASS_HOME) BuyConfirmResult.java

$(CLASS_HOME)/Cart.class: Cart.java
	$(JAVAC) -d $(CLASS_HOME) Cart.java

$(CLASS_HOME)/CartLine.class: CartLine.java
	$(JAVAC) -d $(CLASS_HOME) CartLine.java

$(CLASS_HOME)/Customer.class: Customer.java
	$(JAVAC) -d $(CLASS_HOME) Customer.java

$(CLASS_HOME)/Order.class: Order.java
	$(JAVAC) -d $(CLASS_HOME) Order.java

$(CLASS_HOME)/OrderLine.class: OrderLine.java
	$(JAVAC) -d $(CLASS_HOME) OrderLine.java

$(CLASS_HOME)/ShortBook.class: ShortBook.java
	$(JAVAC) -d $(CLASS_HOME) ShortBook.java

$(CLASS_HOME)/TPCW_Database.class: TPCW_Database.java
	$(JAVAC) -d $(CLASS_HOME) TPCW_Database.java

$(CLASS_HOME)/TPCW_promotional_processing.class: TPCW_promotional_processing.java
	$(JAVAC) -d $(CLASS_HOME) TPCW_promotional_processing.java

$(CLASS_HOME)/TPCW_say_hello.class: TPCW_say_hello.java
	$(JAVAC) -d $(CLASS_HOME) TPCW_say_hello.java

$(CLASS_HOME)/TPCW_Util.class: TPCW_Util.java
	$(JAVAC) -d $(CLASS_HOME) TPCW_Util.java

#servlets
$(SERVLET_HOME)/TPCW_admin_request_servlet.class: TPCW_admin_request_servlet.java
	$(JAVAC) -d $(SERVLET_HOME) TPCW_admin_request_servlet.java 

$(SERVLET_HOME)/TPCW_admin_response_servlet.class: TPCW_admin_response_servlet.java
	$(JAVAC) -d $(SERVLET_HOME) TPCW_admin_response_servlet.java 

$(SERVLET_HOME)/TPCW_best_sellers_servlet.class: TPCW_best_sellers_servlet.java
	$(JAVAC) -d $(SERVLET_HOME) TPCW_best_sellers_servlet.java

$(SERVLET_HOME)/TPCW_buy_confirm_servlet.class: TPCW_buy_confirm_servlet.java
	$(JAVAC) -d $(SERVLET_HOME) TPCW_buy_confirm_servlet.java

$(SERVLET_HOME)/TPCW_buy_request_servlet.class: TPCW_buy_request_servlet.java
	$(JAVAC) -d $(SERVLET_HOME) TPCW_buy_request_servlet.java

$(SERVLET_HOME)/TPCW_customer_registration_servlet.class: TPCW_customer_registration_servlet.java
	$(JAVAC) -d $(SERVLET_HOME) TPCW_customer_registration_servlet.java

$(SERVLET_HOME)/TPCW_execute_search.class: TPCW_execute_search.java
	$(JAVAC) -d $(SERVLET_HOME) TPCW_execute_search.java

$(SERVLET_HOME)/TPCW_home_interaction.class: TPCW_home_interaction.java
	$(JAVAC) -d $(SERVLET_HOME) TPCW_home_interaction.java

$(SERVLET_HOME)/TPCW_new_products_servlet.class: TPCW_new_products_servlet.java
	$(JAVAC) -d $(SERVLET_HOME) TPCW_new_products_servlet.java

$(SERVLET_HOME)/TPCW_order_display_servlet.class: TPCW_order_display_servlet.java
	$(JAVAC) -d $(SERVLET_HOME) TPCW_order_display_servlet.java 

$(SERVLET_HOME)/TPCW_order_inquiry_servlet.class: TPCW_order_inquiry_servlet.java
	$(JAVAC) -d $(SERVLET_HOME) TPCW_order_inquiry_servlet.java 

$(SERVLET_HOME)/TPCW_product_detail_servlet.class: TPCW_product_detail_servlet.java
	$(JAVAC) -d $(SERVLET_HOME) TPCW_product_detail_servlet.java

$(SERVLET_HOME)/TPCW_search_request_servlet.class: TPCW_search_request_servlet.java
	$(JAVAC) -d $(SERVLET_HOME) TPCW_search_request_servlet.java

$(SERVLET_HOME)/TPCW_shopping_cart_interaction.class: TPCW_shopping_cart_interaction.java
	$(JAVAC) -d $(SERVLET_HOME) TPCW_shopping_cart_interaction.java
