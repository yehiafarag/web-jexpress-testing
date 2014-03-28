/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.client.view;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.smartgwt.client.widgets.IButton;

/**
 *
 * @author Yehia Farag
 */
public class ButtonsMenu extends HorizontalPanel {

    final IButton somClustBtn, lineChartBtn, pcaBtn, rankBtn, createGroupBtn, actGroupBtn, exportGroupBtn, saveBtn;

    public IButton getSaveBtn() {
        return saveBtn;
    }

    public IButton getSomClustBtn() {
        return somClustBtn;
    }

    public IButton getLineChartBtn() {
        return lineChartBtn;
    }

    public IButton getPcaBtn() {
        return pcaBtn;
    }

    public IButton getRankBtn() {
        return rankBtn;
    }

    public IButton getCreateGroupBtn() {
        return createGroupBtn;
    }

    public IButton getActGroupBtn() {
        return actGroupBtn;
    }

    public IButton getExportGroupBtn() {
        return exportGroupBtn;
    }

    public ButtonsMenu() {
        setSpacing(5);

        somClustBtn = generateBtn("Hierarchical Clustering", 130);
        this.add(somClustBtn);

        lineChartBtn = generateBtn("Line Chart", 80);
        this.add(lineChartBtn);

        pcaBtn = generateBtn("PCA", 50);
        this.add(pcaBtn);

        rankBtn = generateBtn("Rank Product", 100);
        this.add(rankBtn);

        createGroupBtn = generateBtn("Create Groups/Datasets ", 150);
        this.add(createGroupBtn);

        actGroupBtn = generateBtn("Activate/Deactivate Groups", 150);
        this.add(actGroupBtn);

        exportGroupBtn = generateBtn("Export", 60);
        this.add(exportGroupBtn);

        saveBtn = generateBtn("Save", 50);
        this.add(saveBtn);

    }

    private IButton generateBtn(String btnName, int width) {

        IButton btn = new IButton(btnName);
        btn.setWidth(width);
        btn.setShowRollOver(true);
        btn.setShowDisabled(true);
        btn.setShowDown(true);
        btn.setTitleStyle("stretchTitle");
        btn.disable();
        return btn;
    }

    public void activatMenue() {

        somClustBtn.enable();
        lineChartBtn.enable();
        pcaBtn.enable();
        rankBtn.enable();
        createGroupBtn.enable();
        actGroupBtn.enable();
        saveBtn.enable();
    }
    public void deactivatMenue() {

        somClustBtn.disable();
        lineChartBtn.disable();
        pcaBtn.disable();
        rankBtn.disable();
        createGroupBtn.disable();
        actGroupBtn.disable();
        saveBtn.disable();
    }

}
