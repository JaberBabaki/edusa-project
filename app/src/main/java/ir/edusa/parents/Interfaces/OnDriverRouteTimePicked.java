package ir.edusa.parents.Interfaces;

import java.util.Date;

public interface OnDriverRouteTimePicked {
    public void onTimePicked(Date date, int fromTimeH ,int fromTimeM, int toTimeH, int toTimeM);
}
