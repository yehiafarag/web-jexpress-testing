/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.jexpress.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import org.thechiselgroup.choosel.protovis.client.PVDomAdapter;

/**
 * @author Yehia Farag
 */
public class UnitDomAdapter implements IsSerializable, PVDomAdapter<Unit> {

    @Override
    public Unit[] getChildren(Unit t) {
        return t.children == null ? new Unit[0] : t.children;
    }

    @Override
    public String getNodeName(Unit t) {
        return t.name;
    }

    @Override
    public double getNodeValue(Unit t) {
        return t.value;
    }
}
