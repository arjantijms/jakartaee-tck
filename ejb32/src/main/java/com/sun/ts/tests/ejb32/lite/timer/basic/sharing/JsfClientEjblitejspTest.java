package com.sun.ts.tests.ejb32.lite.timer.basic.sharing;

import com.sun.ts.tests.ejb32.lite.timer.basic.sharing.JsfClient;
import java.net.URL;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;



@ExtendWith(ArquillianExtension.class)
@Tag("platform")
@Tag("ejb_web_profile")
@Tag("web")
@Tag("tck-javatest")

public class JsfClientEjblitejspTest extends com.sun.ts.tests.ejb32.lite.timer.basic.sharing.JsfClient {
    static final String VEHICLE_ARCHIVE = "ejb32_lite_timer_basic_sharing_ejblitejsp_vehicle";

        /**
        EE10 Deployment Descriptors:
        ejb32_lite_timer_basic_sharing_ejblitejsf_vehicle_web: WEB-INF/beans.xml,WEB-INF/faces-config.xml,WEB-INF/web.xml
        ejb32_lite_timer_basic_sharing_ejblitejsp_vehicle_web: 
        ejb32_lite_timer_basic_sharing_ejbliteservlet_vehicle_web: WEB-INF/web.xml
        ejb32_lite_timer_basic_sharing_ejbliteservlet2_vehicle_web: WEB-INF/web.xml

        Found Descriptors:
        War:

        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static WebArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {

        // War
            // the war with the correct archive name
            WebArchive ejb32_lite_timer_basic_sharing_ejblitejsp_vehicle_web = ShrinkWrap.create(WebArchive.class, "ejb32_lite_timer_basic_sharing_ejblitejsp_vehicle_web.war");
            // The class files
            ejb32_lite_timer_basic_sharing_ejblitejsp_vehicle_web.addClasses(
            com.sun.ts.tests.ejb30.common.helper.TestFailedException.class,
            com.sun.ts.tests.ejb32.lite.timer.basic.sharing.StatelessTimerBean.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.ejb30.timer.common.JsfClientBase.class,
            com.sun.ts.tests.ejb32.lite.timer.basic.sharing.JsfClient.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.ejb30.common.helper.Helper.class,
            com.sun.ts.tests.ejb30.common.lite.EJBLiteClientBase.class,
            com.sun.ts.tests.ejb32.lite.timer.basic.sharing.SingletonTimerBean.class,
            com.sun.ts.tests.ejb30.timer.common.ClientBase.class,
            com.sun.ts.tests.ejb32.lite.timer.basic.sharing.EJBLiteJSPTag.class,
            com.sun.ts.tests.ejb30.timer.common.TimerBeanBaseWithoutTimeOutMethod.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.lib.harness.ServiceEETest.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.ejb30.common.lite.NumberIF.class,
            com.sun.ts.tests.ejb32.lite.timer.basic.sharing.HttpServletDelegate.class,
            com.sun.ts.tests.common.vehicle.ejbliteshare.EJBLiteClientIF.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.ejb30.timer.common.TimerInfo.class,
            com.sun.ts.tests.common.vehicle.ejbliteshare.ReasonableStatus.class,
            com.sun.ts.tests.ejb30.timer.common.TimeoutStatusBean.class,
            com.sun.ts.tests.ejb32.lite.timer.basic.sharing.SharingTimerBeanBase.class,
            com.sun.ts.tests.ejb30.common.lite.NumberEnum.class,
            com.sun.ts.tests.ejb32.lite.timer.basic.sharing.Client.class,
            com.sun.ts.tests.ejb30.common.lite.EJBLiteJsfClientBase.class,
            com.sun.ts.tests.ejb30.timer.common.TimerUtil.class,
            com.sun.ts.lib.harness.EETest.SetupException.class,
            com.sun.ts.tests.ejb32.lite.timer.basic.sharing.TimerIF.class
            );
            ejb32_lite_timer_basic_sharing_ejblitejsp_vehicle_web.addClass("com.sun.ts.tests.ejb32.lite.timer.basic.sharing.Client$1.class");
            ejb32_lite_timer_basic_sharing_ejblitejsp_vehicle_web.addClass("com.sun.ts.tests.ejb32.lite.timer.basic.sharing.JsfClient$1.class");

            // The web.xml descriptor
            URL warResURL = JsfClient.class.getResource("/vehicle/ejblitejsp/ejblitejsp_vehicle_web.xml");
            if(warResURL != null) {
              ejb32_lite_timer_basic_sharing_ejblitejsp_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = JsfClient.class.getResource("//vehicle/ejblitejsp/ejblitejsp_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              ejb32_lite_timer_basic_sharing_ejblitejsp_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }
            // Web content
            warResURL = JsfClient.class.getResource("/com/sun/ts/tests/common/vehicle/ejblitejsp/ejblitejsp_vehicle.jsp");
            ejb32_lite_timer_basic_sharing_ejblitejsp_vehicle_web.addAsWebResource(warResURL, "/ejblitejsp_vehicle.jsp");

           archiveProcessor.processWebArchive(ejb32_lite_timer_basic_sharing_ejblitejsp_vehicle_web, JsfClient.class, warResURL);

        return ejb32_lite_timer_basic_sharing_ejblitejsp_vehicle_web;
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsp")
        public void createTimerRollbackStateless() {
            super.createTimerRollbackStateless();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsp")
        public void createTimerRollbackSingleton() {
            super.createTimerRollbackSingleton();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsp")
        public void createVerifyRecurringTimerStateless() {
            super.createVerifyRecurringTimerStateless();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsp")
        public void createVerifyRecurringTimerSingleton() {
            super.createVerifyRecurringTimerSingleton();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsp")
        public void accessTimersStateless() {
            super.accessTimersStateless();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsp")
        public void accessTimersSingleton() {
            super.accessTimersSingleton();
        }


}