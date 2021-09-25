package com.smattme.demoserver.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import com.smattme.demoserver.config.CachingHttpRequestWrapper;
import com.smattme.demoserver.config.Routes;
import com.smattme.demoserver.helpers.CryptoHelper;


public class AesEncryptionFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(AesEncryptionFilter.class);
    private String aesSecretKey;

    public AesEncryptionFilter(String aesSecretKey) {
        this.aesSecretKey = aesSecretKey;
    }



    @Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {


        String uri = request.getRequestURI();
        if(logger.isDebugEnabled())
            logger.debug("PATH IN SignatureAuthenticationFilter : " + uri);

        //only run this filter if the URL starts with the /core/token/bank/debit
        return !uri.startsWith(Routes.Bank.DEBIT_CUSTOMER);
    }



    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain chain) throws IOException, ServletException {


        if(shouldNotFilter(httpServletRequest)) {
            logger.debug("skipping AesEncryptionFilter");
            chain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        //excluding paths
        logger.debug("Running AesEncryptionFilter");
        
        //this will allow us read the request body more than once
        var requestWrapper = new CachingHttpRequestWrapper(httpServletRequest);
        var encryptedBodyInBase64 = requestWrapper.getRequestBodyString();
        
        var decryptedBody = CryptoHelper.decryptDataAES(encryptedBodyInBase64, aesSecretKey.getBytes());
        
        //set the decrypted body as request body
        requestWrapper.setRequestBody(decryptedBody.getBytes());
        
        //otherwise everything went well, let's proceed with the request
        //not that we're using requestWrapper and not httpServletRequest because we've read the body
        //once
        chain.doFilter(requestWrapper, httpServletResponse);
    }


}
