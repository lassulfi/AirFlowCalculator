package com.poseidonos.airflowcalculator.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Farm Entity class
 */
public class Farm implements Parcelable {

    private String nameSite; //Site name
    private int panelGen; //Panel Generation
    private int availNumComps; //Available Compressors
    private int compFlow; //Compressor Output CFM (FAD)
    private int numComps; //Active Compressors
    private int numPens; //Number of Pens
    private int chanPerPen; //Channels per Pen
    private int activePens; //Active Pens
    private int chnlWalkway; //Number of Walkway Channels
    private int activeChnlWalk; //Active Walkway Channels
    private int readPressure; //Panel Set Pressure (psi)
    private double targetFlowDisplay; //Flow Meter Reading (Target)
    private double stdReading; //Comparative Standard Meter

    public Farm() {
        this.targetFlowDisplay = 0.0;
        this.stdReading = 0.0;
    }

    protected Farm(Parcel in) {
        nameSite = in.readString();
        panelGen = in.readInt();
        availNumComps = in.readInt();
        compFlow = in.readInt();
        numComps = in.readInt();
        numPens = in.readInt();
        chanPerPen = in.readInt();
        activePens = in.readInt();
        chnlWalkway = in.readInt();
        activeChnlWalk = in.readInt();
        readPressure = in.readInt();
        targetFlowDisplay = in.readDouble();
        stdReading = in.readDouble();
    }

    public static final Creator<Farm> CREATOR = new Creator<Farm>() {
        @Override
        public Farm createFromParcel(Parcel in) {
            return new Farm(in);
        }

        @Override
        public Farm[] newArray(int size) {
            return new Farm[size];
        }
    };

    public String getNameSite() {
        return nameSite;
    }

    public void setNameSite(String nameSite) {
        this.nameSite = nameSite;
    }

    public int getPanelGen() {
        return panelGen;
    }

    public void setPanelGen(int panelGen) {
        this.panelGen = panelGen;
    }

    public int getAvailNumComps() {
        return availNumComps;
    }

    public void setAvailNumComps(int availNumComps) {
        this.availNumComps = availNumComps;
    }

    public int getCompFlow() {
        return compFlow;
    }

    public void setCompFlow(int compFlow) {
        this.compFlow = compFlow;
    }

    public int getNumComps() {
        return numComps;
    }

    public void setNumComps(int numComps) {
        this.numComps = numComps;
    }

    public int getNumPens() {
        return numPens;
    }

    public void setNumPens(int numPens) {
        this.numPens = numPens;
    }

    public int getChanPerPen() {
        return chanPerPen;
    }

    public void setChanPerPen(int chanPerPen) {
        this.chanPerPen = chanPerPen;
    }

    public int getActivePens() {
        return activePens;
    }

    public void setActivePens(int activePens) {
        this.activePens = activePens;
    }

    public int getChnlWalkway() {
        return chnlWalkway;
    }

    public void setChnlWalkway(int chnlWalkway) {
        this.chnlWalkway = chnlWalkway;
    }

    public int getActiveChnlWalk() {
        return activeChnlWalk;
    }

    public void setActiveChnlWalk(int activeChnlWalk) {
        this.activeChnlWalk = activeChnlWalk;
    }

    public int getReadPressure() {
        return readPressure;
    }

    public void setReadPressure(int readPressure) {
        this.readPressure = readPressure;
    }

    public double getTargetFlowDisplay() {
        return targetFlowDisplay;
    }

    public void setTargetFlowDisplay(double targetFlowDisplay) {
        this.targetFlowDisplay = targetFlowDisplay;
    }

    public double getStdReading() {
        return stdReading;
    }

    public void setStdReading(double stdReading) {
        this.stdReading = stdReading;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(nameSite);
        parcel.writeInt(panelGen);
        parcel.writeInt(availNumComps);
        parcel.writeInt(compFlow);
        parcel.writeInt(numComps);
        parcel.writeInt(numPens);
        parcel.writeInt(chanPerPen);
        parcel.writeInt(activePens);
        parcel.writeInt(chnlWalkway);
        parcel.writeInt(activeChnlWalk);
        parcel.writeInt(readPressure);
        parcel.writeDouble(targetFlowDisplay);
        parcel.writeDouble(stdReading);
    }
}
