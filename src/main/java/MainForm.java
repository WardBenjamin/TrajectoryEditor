import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
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
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    private void action_create(ActionEvent evt)
    {
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
        if(chartFrame != null)
            chartFrame.setVisible(false);
        waypoints = new ArrayList<>();
        textArea_waypointBox.setText("");
        System.out.println("Reset path and stored waypoints!");
    }

    private void action_graph(ActionEvent evt) {
        Trajectory trajectory = finalizeTrajectory();

        if(trajectory == null) {
            return;
        }

        XYSeriesCollection collection = new XYSeriesCollection();
        XYSeries series = new XYSeries("robot");

        for (int i = 0; i < trajectory.segments.length; i++)
        {
            Trajectory.Segment segment = trajectory.segments[i];
            series.add(segment.x, segment.y);
        }

        JFreeChart chart = ChartFactory.createScatterPlot("Robot", "X", "Y",
                collection, PlotOrientation.VERTICAL, true, true, false);
        chartFrame = new ChartFrame("Robot", chart);
        chartFrame.pack();
        chartFrame.setVisible(true);
    }

    private void action_export(ActionEvent evt) {}

    private Trajectory finalizeTrajectory()
    {
        double max_velocity = 1.7, max_acceleration = 2.0, max_jerk = 60.0;
        Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_QUINTIC,
                Trajectory.Config.SAMPLES_HIGH, 0.05, max_velocity, max_acceleration, max_jerk); // Delta time 0.05 = 1/20
        Waypoint[] array = new Waypoint[waypoints.size()];

        if(waypoints.size() > 0) {
            System.out.println("Finalized trajectory with " + array.length + " waypoints!");
            return Pathfinder.generate(waypoints.toArray(array), config);
        }

        System.out.println("Failed to finalize trajectory because it had zero elements!");
        return null;
    }
}
