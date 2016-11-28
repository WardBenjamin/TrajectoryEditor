import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.DomainInfo;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * Created by Benjamin Ward on 11/25/2016.
 */
public class MainForm {
    public JPanel WaypointEditor;
    private JScrollPane scrollPane_waypointBox;
    private JTextArea textArea_waypointBox;
    private JLabel label_X;
    private JTextField textField_xInput;
    private JLabel label_Y;
    private JTextField textField_yInput;
    private JLabel label_exitAngle;
    private JTextField textField_exitAngleInput;
    private JButton button_create;
    private JButton button_reset;
    private JButton button_view;
    private JButton button_export;
    private JLabel label_maxVel;
    private JTextField textField_maxVel;
    private JLabel label_maxAccl;
    private JTextField textField_maxAccl;
    private JLabel label_maxJerk;
    private JTextField textField_maxJerk;
    private JLabel label_deltaTime;
    private JTextField textField_deltaTimeHZ;
    private JLabel label_maxVel_unit;
    private JLabel label_maxAccl_unit;
    private JLabel label_maxJerk_unit;
    private JLabel label_deltaTime_unit;
    private JButton button_saveSettings;

    private double max_vel, max_accl, max_jerk, deltaTime;
    private ArrayList<Waypoint> waypoints = new ArrayList<>();
    private ChartFrame chartFrame;

    public MainForm() {
        button_create.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainForm.this.action_create(e);
            }
        });
        button_reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainForm.this.action_reset(e);
            }
        });
        button_view.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainForm.this.action_graph(e);
            }
        });
        button_export.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainForm.this.action_export(e);
            }
        });
        button_saveSettings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainForm.this.action_settings(e);
            }
        });

        // Set variables with default values
        max_vel = Double.valueOf(this.textField_maxVel.getText());
        max_accl = Double.valueOf(this.textField_maxAccl.getText());
        max_jerk = Double.valueOf(this.textField_maxJerk.getText());
        deltaTime = 1.0 / Double.valueOf(this.textField_deltaTimeHZ.getText());
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    private void action_create(ActionEvent evt) {
        try {
            double x = Double.valueOf(this.textField_xInput.getText());
            double y = Double.valueOf(this.textField_yInput.getText());
            double exitAngle = Pathfinder.d2r(Double.valueOf(this.textField_exitAngleInput.getText()));

            Waypoint waypoint = new Waypoint(x, y, exitAngle);
            waypoints.add(waypoint);

            System.out.println("Number of Waypoints: " + waypoints.size());

            this.textArea_waypointBox.setText(this.textArea_waypointBox.getText()
                    + "X: " + x + "  Y: " + y + "   A: " + exitAngle + " WAYPOINT: " + waypoints.size() + "\n");


        } catch (NullPointerException | NumberFormatException e) {
            System.out.println("Invalid number input. Try again.");
        }
    }

    private void action_reset(ActionEvent evt) {
        if (chartFrame != null)
            chartFrame.setVisible(false);
        waypoints = new ArrayList<>();
        textArea_waypointBox.setText("");
        System.out.println("Reset path and stored waypoints!");
    }

    private void action_graph(ActionEvent evt) {
        Trajectory trajectory = finalizeTrajectory(CalculationSpeed.FAST);

        if (trajectory == null) {
            return;
        }

        XYSeriesCollection collection = new XYSeriesCollection();
        XYSeries series = new XYSeries("robot");

        for (int i = 0; i < trajectory.segments.length; i++) {
            Trajectory.Segment segment = trajectory.segments[i];
            series.add(segment.x, segment.y);
            //System.out.println("X, Y: " + segment.x + ", " + segment.y);
        }

        collection.addSeries(series);

        if(chartFrame != null) {
            chartFrame.dispose();
            //chartFrame.setVisible(false);
        }
        JFreeChart chart = ChartFactory.createScatterPlot("Robot", "X", "Y",
                collection, PlotOrientation.VERTICAL, true, true, false);
        chartFrame = new ChartFrame("Robot", chart);
        chartFrame.pack();
        chartFrame.setVisible(true);
    }

    private void action_export(ActionEvent evt) {
        Trajectory trajectory = finalizeTrajectory(CalculationSpeed.SLOW);
        String path = "." + File.separator + "output.csv";
        System.out.println("Exporting trajectory to: " + path);
        Pathfinder.writeToCSV(new File(path), trajectory);
    }

    private void action_settings(ActionEvent evt) {
        boolean failed = false;
        try { max_vel = Double.valueOf(this.textField_maxVel.getText()); }
        catch(Exception e) { System.out.println("Maximum velocity invalid."); failed = true; }

        try { max_accl = Double.valueOf(this.textField_maxAccl.getText()); }
        catch(Exception e) { System.out.println("Maximum acceleration invalid."); failed = true; }

        try { max_jerk = Double.valueOf(this.textField_maxJerk.getText()); }
        catch(Exception e) { System.out.println("Maximum jerk invalid."); failed = true; }

        try { deltaTime = 1.0 / Double.valueOf(this.textField_deltaTimeHZ.getText()); } // Converts hz to seconds
        catch(Exception e) { System.out.println("Delta time invalid."); failed = true; }

        if(!failed)
            System.out.println("Settings updated successfully!");
        else
            System.out.println("Settings not updated successfully; check above for options with invalid input.");
    }

    enum CalculationSpeed
    {
        SLOW,
        FAST
    }

    private Trajectory finalizeTrajectory(CalculationSpeed calcSpeed) {
        Trajectory.Config config;
        if(calcSpeed == CalculationSpeed.FAST)
            config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_QUINTIC, Trajectory.Config.SAMPLES_HIGH, deltaTime, max_vel, max_accl, max_jerk);
        else
            config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_QUINTIC, Trajectory.Config.SAMPLES_LOW, deltaTime * 3, max_vel, max_accl, max_jerk);
        Waypoint[] array = new Waypoint[waypoints.size()];

        Trajectory trajectory = null;

        if (waypoints.size() > 0) {
            System.out.println("Finalized trajectory with " + array.length + " waypoints!");
            try {
                trajectory = Pathfinder.generate(waypoints.toArray(array), config);
            }
            catch(Exception e) {
                System.out.println("Failed to finalize trajectory because settings are invalid or path not able to be constructed!");
            }
        }
        else System.out.println("Failed to finalize trajectory because it had zero elements!");

        return trajectory;
    }
}
