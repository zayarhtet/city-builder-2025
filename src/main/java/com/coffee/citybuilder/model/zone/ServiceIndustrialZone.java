package com.coffee.citybuilder.model.zone;

import com.coffee.citybuilder.model.CellItem;
import com.coffee.citybuilder.model.Position;

import static com.coffee.citybuilder.resource.Constant.Initial_Employee;

public class ServiceIndustrialZone extends Zone {
    private int workers = 0;
    private int workerCapacity = 40;
    public ServiceIndustrialZone(Position p) {
        super(p); super.ct = CellItem.SERVICE_INDUSTRIAL;
        super.numOfPeople = Initial_Employee;
    }
    public void increaseWorkers(int value){
        workers = Math.min(workers+value,workerCapacity);
//        System.out.println(workers + " out of " + workerCapacity);
    }
}
