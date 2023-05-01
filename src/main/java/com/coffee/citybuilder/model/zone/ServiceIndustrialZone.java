package com.coffee.citybuilder.model.zone;

import com.coffee.citybuilder.model.CellItem;
import com.coffee.citybuilder.model.Position;

import static com.coffee.citybuilder.resource.Constant.Initial_Employee;

public class ServiceIndustrialZone extends Zone {
    public ServiceIndustrialZone(Position p) {
        super(p); super.ct = CellItem.SERVICE_INDUSTRIAL;
    }
}
