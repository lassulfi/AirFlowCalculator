package com.poseidonos.airflowcalculator.controller;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class FarmControllerTest {

    private FarmController controller;

    @Before
    public void setup(){
        controller = new FarmController();

    }

    @Test
    public void testCalculate(){
        controller.createNewFarm();
        controller.setNameSite("Site A");
        controller.setPanelGen(4);
        controller.setCompFlow(400);
        controller.setNumComps(2);
        controller.setNumPens(10);
        controller.setChanPerPen(4);
        controller.setActivePens(10);
        controller.setChnlWalkway(20);
        controller.setActiveChnlWalk(20);
        controller.setReadPressure(35);

        controller.calculate();

        Assert.assertEquals(controller.getTargetFlowDisplay(), 45,0.1);
        Assert.assertEquals(controller.getStdReading(),7.2,0.1);
    }
}
