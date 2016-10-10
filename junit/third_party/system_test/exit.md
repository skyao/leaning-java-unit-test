System.exit()
------
如果你的代码调用了System.exit()，ExpectedSystemExit可以帮你测试它的调用。但是没有办法使用JUnit的断言了,你可以提供一个断言对象到ExpectedSystemExit(这里只能使用JUnit的Assertion,不能使用assertj)
```java
    package com.whtr.service.authentication.resources.impl;

    import com.whtr.dolphin.contract.BusinessStatusRuntimeException;
    import com.whtr.dolphin.contract.SystemStatusRuntimeException;
    import com.whtr.service.authentication.resources.UserResource;
    import com.whtr.service.authentication.resources.pojo.UserPojo;
    import com.whtr.soa.contracts.registration.registrationservice.CustomerModel;
    import com.whtr.soa.contracts.registration.registrationservice.IRegistrationService;
    import com.whtr.soa.contracts.registration.registrationservice.ValidateCustomerReq;
    import com.whtr.soa.contracts.registration.registrationservice.ValidateCustomerResp;

    import org.junit.After;
    import org.junit.Before;
    import org.junit.Test;
    import org.mockito.InjectMocks;
    import org.mockito.Mock;
    import org.mockito.MockitoAnnotations;


    import java.rmi.RemoteException;

    import static org.assertj.core.api.Assertions.assertThat;
    import static org.mockito.Mockito.reset;
    import static org.mockito.Mockito.when;

    public class UserResourceImplTest {
        @InjectMocks
        private UserResource userResource;

        @Mock
        private IRegistrationService iRegistrationService;

        @Before
        public void setUp() throws Exception {
            userResource = new UserResourceImpl();
            MockitoAnnotations.initMocks(this);
        }

        @After
        public void tearDown() throws Exception {
            reset(iRegistrationService);
        }

        @Test
        public void testValidateCustomer() throws Exception {
            String phone = "123456";
            String password = "123456";

            ValidateCustomerReq req = new ValidateCustomerReq();
            req.setPhone(phone);
            req.setPassword(password);

            ValidateCustomerResp validateCustomerResp = new ValidateCustomerResp();
            validateCustomerResp.setSuccess(true);

            CustomerModel customerModel = new CustomerModel();
            customerModel.setId(123);

            validateCustomerResp.setCustomer(customerModel);
            when(iRegistrationService.validateCustomer(req)).thenReturn(validateCustomerResp);
            UserPojo validate = userResource.validateCustomer(phone, password);
            assertThat(validate).isNotNull();
        }

        @Test(expected = BusinessStatusRuntimeException.class)
        public void testValidateCustomer_false() throws Exception {
            String phone = "123456";
            String password = "123456";

            ValidateCustomerReq req = new ValidateCustomerReq();
            req.setPhone(phone);
            req.setPassword(password);

            ValidateCustomerResp validateCustomerResp = new ValidateCustomerResp();
            validateCustomerResp.setSuccess(false);
            validateCustomerResp.setErrorCode(1);

            CustomerModel customerModel = new CustomerModel();
            customerModel.setId(123);

            validateCustomerResp.setCustomer(customerModel);
            when(iRegistrationService.validateCustomer(req)).thenReturn(validateCustomerResp);
            userResource.validateCustomer(phone, password);
        }


        @Test(expected = BusinessStatusRuntimeException.class)
        public void testValidateCustomer_false2() throws Exception {
            String phone = "123456";
            String password = "123456";

            ValidateCustomerReq req = new ValidateCustomerReq();
            req.setPhone(phone);
            req.setPassword(password);

            ValidateCustomerResp validateCustomerResp = new ValidateCustomerResp();
            validateCustomerResp.setSuccess(false);
            validateCustomerResp.setErrorCode(2);

            CustomerModel customerModel = new CustomerModel();
            customerModel.setId(123);

            validateCustomerResp.setCustomer(customerModel);
            when(iRegistrationService.validateCustomer(req)).thenReturn(validateCustomerResp);
            userResource.validateCustomer(phone, password);
        }


        @Test(expected = SystemStatusRuntimeException.class)
        public void testValidateCustomer_exception() throws Exception {
            String phone = "123456";
            String password = "123456";

            ValidateCustomerReq req = new ValidateCustomerReq();
            req.setPhone(phone);
            req.setPassword(password);

            ValidateCustomerResp validateCustomerResp = new ValidateCustomerResp();
            validateCustomerResp.setSuccess(false);

            CustomerModel customerModel = new CustomerModel();
            customerModel.setId(123);

            validateCustomerResp.setCustomer(customerModel);
            when(iRegistrationService.validateCustomer(req)).thenThrow(new RemoteException());
            userResource.validateCustomer(phone, password);
        }
    }
```