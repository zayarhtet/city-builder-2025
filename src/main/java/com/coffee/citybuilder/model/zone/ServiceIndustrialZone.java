package com.coffee.citybuilder.model.zone;

import com.coffee.citybuilder.model.CellItem;
import com.coffee.citybuilder.model.Position;

import static com.coffee.citybuilder.resource.Constant.Initial_Employee;

/**
 * Class used to store service or industrial zones in the city.
 * 
 * These zones generate jobs and generate money in the form of taxes.
 */
public class ServiceIndustrialZone extends Zone {
    public ServiceIndustrialZone(Position p) {
        super(p);
        super.ct = CellItem.SERVICE_INDUSTRIAL;
    }
}
