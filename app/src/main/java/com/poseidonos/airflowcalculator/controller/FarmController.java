package com.poseidonos.airflowcalculator.controller;

import android.os.Parcel;
import android.os.Parcelable;

import com.poseidonos.airflowcalculator.entities.Farm;

public class FarmController implements Parcelable {

    private Farm mFarm;

    public FarmController() {
    }

    protected FarmController(Parcel in) {
        mFarm = in.readParcelable(Farm.class.getClassLoader());
    }

    public static final Creator<FarmController> CREATOR = new Creator<FarmController>() {
        @Override
        public FarmController createFromParcel(Parcel in) {
            return new FarmController(in);
        }

        @Override
        public FarmController[] newArray(int size) {
            return new FarmController[size];
        }
    };

    /**
     * Creates a new farm
     */
    public void createNewFarm(){
        this.mFarm = new Farm();
    }

    /**
     * Loads an existing farm to the controller
     * @param farm
     */
    public void loadFarm(Farm farm){
        this.mFarm = farm;
    }

    /**
     * Set the site name
     * @param nameSite
     */
    public void setNameSite(String nameSite){
        this.mFarm.setNameSite(nameSite);
    }

    /**
     * Returns the site name
     * @return String
     */
    public String getNameSite(){
        return this.mFarm.getNameSite();
    }

    /**
     * Set the number of panel generation
     * @param panelGen
     */
    public void setPanelGen(int panelGen){
        this.mFarm.setPanelGen(panelGen);
    }

    /**
     * Get the number of panel generation
     * @return int
     */
    public int getPanelGen(){
        return this.mFarm.getPanelGen();
    }

    /**
     * Set the number of available compressors
     * @param availNumComps
     */
    public void setAvailNumComps(int availNumComps){
        this.mFarm.setAvailNumComps(availNumComps);
    }

    /**
     * Get the number of available compressors
     * @return int
     */
    public int getAvailNumComps(){
        return this.mFarm.getAvailNumComps();
    }

    /**
     * Set the value of Compressor Output CFM (FAD)
     * @param compFlow
     */
    public void setCompFlow(int compFlow){
        this.mFarm.setCompFlow(compFlow);
    }

    /**
     * Get the value of Compressor Output CFM (FAD)
     * @return int
     */
    public int getCompFlow(){
        return this.mFarm.getCompFlow();
    }

    /**
     * Set the number of active compressors
     * @param numComps
     */
    public void setNumComps(int numComps){
        this.mFarm.setNumComps(numComps);
    }

    /**
     * Get the number of active compressors
     * @return int
     */
    public int getNumComps(){
        return this.mFarm.getNumComps();
    }

    /**
     * Set the number of pens
     * @param numPens
     */
    public void setNumPens(int numPens){
        this.mFarm.setNumPens(numPens);
    }

    /**
     * Get the number of pens
     * @return int
     */
    public int getNumPens(){
        return this.mFarm.getNumPens();
    }

    /**
     * Set the number of channels per pen
     * @param chanPerPen
     */
    public void setChanPerPen(int chanPerPen){
        this.mFarm.setChanPerPen(chanPerPen);
    }

    /**
     * Get the number of channels per pen
     * @return int
     */
    public int getChanPerPen(){
        return this.mFarm.getChanPerPen();
    }

    /**
     * Set the number of active pens
     * @param activePens
     */
    public void setActivePens(int activePens){
        this.mFarm.setActivePens(activePens);
    }

    /**
     * Get the number of active pens
     * @return int
     */
    public int getActivePens(){
        return this.mFarm.getActivePens();
    }

    /**
     * Set the Number of Walkway Channels
     * @param chnlWalkway
     */
    public void setChnlWalkway(int chnlWalkway){
        this.mFarm.setChnlWalkway(chnlWalkway);
    }

    /**
     * Get the Number of Walkway Channels
     * @return int
     */
    public int getChnlWalkway(){
        return this.mFarm.getChnlWalkway();
    }

    /**
     * Set the number of Active Walkway Channels
     * @param activeChnlWalk
     */
    public void setActiveChnlWalk(int activeChnlWalk){
        this.mFarm.setActiveChnlWalk(activeChnlWalk);
    }

    /**
     * Get the number of Active Walkway Channels
     * @return int
     */
    public int getActiveChnlWalk(){
        return this.mFarm.getActiveChnlWalk();
    }

    /**
     * Set the value of Panel Set Pressure (psi)
     * @param readPressure
     */
    public void setReadPressure(int readPressure){
        this.mFarm.setReadPressure(readPressure);
    }

    /**
     * Get the value of Panel Set Pressure (psi)
     * @return double
     */
    public double getReadPressure(){
        return this.mFarm.getReadPressure();
    }

    /**
     * Get the value of Flow Meter Reading (Target)
     * @return double
     */
    public double getTargetFlowDisplay(){
        return this.mFarm.getTargetFlowDisplay();
    }

    /**
     * Get the value of Comparative Standard Meter
     * @return
     */
    public double getStdReading(){
        return this.mFarm.getStdReading();
    }

    /**
     * Calculates the targetFlowDisplay and the StdReading for a farm
     */
    public void calculate(){
        double totalFlow = this.mFarm.getCompFlow() * this.mFarm.getNumComps();
        double activeOutlets = this.mFarm.getChanPerPen() * this.mFarm.getActivePens()
                + this.mFarm.getActiveChnlWalk();
        double flowPerChnl = totalFlow / activeOutlets;
        double calcPressure = calculateCalcPressure();
        double pressCorr = Math.sqrt((this.mFarm.getReadPressure() + 14.7)/(calcPressure + 14.7));
        double stdCorr = Math.sqrt((this.mFarm.getReadPressure() + 14.7)/(0.0 + 14.7));
        double targetReadingFlow = flowPerChnl / pressCorr;
        double stdReading = flowPerChnl / stdCorr;
        final double MAXSTANDARDFLOW = 16;
        double maxFlowRate = MAXSTANDARDFLOW * pressCorr;
        double targetReadingPercent = Math.round(100 * flowPerChnl / maxFlowRate);

        this.mFarm.setTargetFlowDisplay((this.mFarm.getPanelGen() == 4) ? targetReadingPercent : targetReadingFlow);

        this.mFarm.setStdReading(stdReading);

    }

    private int calculateCalcPressure(){
        int calcPressure = 0;
        switch (this.mFarm.getPanelGen()){
            case 1:
                calcPressure = 90;
                break;
            case 2:
                calcPressure = 60;
                break;
            case 4:
                calcPressure = 0;
        }

        return calcPressure;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(mFarm, i);
    }
}
