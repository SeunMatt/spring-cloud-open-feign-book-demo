package com.smattme.demoserver.config;

public class Routes {
	
	

	public static final String SECURED = "/core";
	public static final String SECURED_BASIC = "/core/basic";
	public static final String SECURED_TOKEN = "/core/token";
	public static final String SECURED_SIGNATURE = "/core/sig";
    public static final String NON_SECURED = "/basic";
    
    public static class Index {
    	public static final String INDEX = "/";
    	public static final String INDEX_ECHO = "/user/{id}";
    }
	
	public static class Exception {
        public static final String DEFAULT_ERROR = "/error";
    }
	
	public static class Product {
		public static final String PRODUCTS = SECURED_BASIC + "/products";
		public static final String PRODUCT = SECURED_BASIC + "/products/{productName}";
	}
	
	public static class Orders {
		public static final String PLACE_ORDER = SECURED_BASIC + "/orders";
	}
	
	public static class Refund {
		public static final String REFUND = SECURED_TOKEN + "/refunds";
	}
	
	public static class Cart {
		public static final String ADD_ITEMS = SECURED_TOKEN + "/cart/items";
	}
	
	public static class Bank {
		public static final String DEBIT_CUSTOMER = SECURED_TOKEN + "/bank/debit";
		
	}
	
	public static class Card {
		public static final String GET_CARD_DETAILS = SECURED_TOKEN + "/cards/{customerId}";
	}
	
	public static class Wallet {
		public static final String CREDIT_CUSTOMER = SECURED_TOKEN + "/wallet/credit";
	}
	
	public static class SMS {
		public static final String SEND_SMS = SECURED_SIGNATURE + "/sms/send";
	}
	
	public static class Email {
		public static final String SEND_EMAIL = NON_SECURED + "/email/send";
	}
	
	public static class Health {
		public static final String HEALTH_STATUS = NON_SECURED + "/health";
	}


}
