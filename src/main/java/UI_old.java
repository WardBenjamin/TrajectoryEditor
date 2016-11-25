import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Created by Benjamin Ward on 11/25/2016.
 */
public class UI_old extends JFrame {
    private JButton button_viewPath;
    private JButton button_reset;
    private JButton button_export;
    private JButton button_create;

    private JTextField textField_pathName;
    private JScrollPane scrollPane_waypointBox;
    private JTextArea textArea_waypointsBox;

    private JLabel label_newWaypoint;
    private JTextField textField_xInput;
    private JTextField textField_yInput;
    private JTextField textField_exitAngleInput;

    ArrayList<Waypoint> waypoints = new ArrayList<>();
    Path path;

    public UI_old() {
        /*try {
            path = Paths.get(UI_old.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        }
        catch (Exception e) {*/
        path = Paths.get("").toAbsolutePath();
        //}

        initComponents();
        this.textArea_waypointsBox.setEditable(false);
    }

    private void initComponents() {
        this.button_viewPath = new JButton();
        this.button_reset = new JButton();
        this.button_export = new JButton();
        this.button_create = new JButton();

        this.textField_pathName = new JTextField();
        this.scrollPane_waypointBox = new JScrollPane();
        this.textArea_waypointsBox = new JTextArea();

        this.label_newWaypoint = new JLabel();
        this.textField_xInput = new JTextField();
        this.textField_yInput = new JTextField();
        this.textField_exitAngleInput = new JTextField();

        setDefaultCloseOperation(3);

        this.button_viewPath.setText("VIEW PATH");
        this.button_viewPath.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                UI_old.this.viewPathActionPerformed(evt);
            }
        });

        this.button_reset.setText("RESET");
        this.button_reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                UI_old.this.resetActionPerformed(evt);
            }
        });

        this.button_export.setText("EXPORT");
        this.button_export.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                UI_old.this.exportActionPerformed(evt);
            }
        });

        this.button_create.setText("CREATE");
        this.button_create.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                UI_old.this.createButtonActionPerformed(evt);
            }
        });

        this.scrollPane_waypointBox.setEnabled(false);

        this.textArea_waypointsBox.setColumns(20);
        this.textArea_waypointsBox.setFont(new Font("Arial", 0, 13));
        this.textArea_waypointsBox.setRows(5);
        this.scrollPane_waypointBox.setViewportView(this.textArea_waypointsBox);

        this.label_newWaypoint.setText("Add New Waypoint");
        this.textField_xInput.setText("X");
        this.textField_yInput.setText("Y");
        this.textField_exitAngleInput.setText("Exit Angle");

        this.textField_pathName.setText(path.toString());
        this.textField_pathName.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                UI_old.this.pathNameActionPerformed(evt);
            }
        });

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(this.scrollPane_waypointBox, -2, 339, -2)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup().addGap(0, 34, 32767)
                                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                                                .addComponent(this.label_newWaypoint).addGap(47, 47, 47))
                                                                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                                                        .addComponent(this.button_create, -1, -1, 32767)
                                                                                        .addComponent(this.textField_exitAngleInput, -2, 71, -2)
                                                                                )
                                                                                .addGap(40, 40, 40)
                                                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                                                        .addComponent(this.textField_yInput).addComponent(this.textField_xInput, -2, 56, -2)
                                                                                )
                                                                                .addGap(18, 18, 18)
                                                                        )
                                                                )
                                                        )
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(this.button_viewPath)
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, -1, 32767)
                                                                .addComponent(this.button_reset)
                                                                .addGap(33, 33, 33)
                                                        )
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(this.button_export)
                                                                .addContainerGap()
                                                        )
                                                )
                                        )
                                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(this.textField_pathName)
                                                .addContainerGap()
                                        )
                                )
                        )
        );

        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(this.textField_pathName, -2, -1, -2)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 40, 32767)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(this.scrollPane_waypointBox, -2, 136, -2)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(this.label_newWaypoint)
                                                .addGap(17, 17, 17)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(this.textField_xInput, -2, -1, -2)
                                                        .addComponent(this.textField_exitAngleInput, -2, -1, -2)
                                                )
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(this.textField_yInput, -2, -1, -2)
                                                        .addComponent(this.button_create)
                                                )
                                        )
                                        .addGroup(GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(this.button_viewPath)
                                                        .addComponent(this.button_reset)
                                                )
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(this.button_export)
                                        )
                                )
                                .addGap(15, 15, 15)
                        )
        );

        pack();
    }

    private void createButtonActionPerformed(ActionEvent evt) {
        try {
            double x = Double.valueOf(this.textField_xInput.getText());
            double y = Double.valueOf(this.textField_yInput.getText());
            double exitAngle = Double.valueOf(this.textField_exitAngleInput.getText());

            Waypoint waypoint = new Waypoint(x, y, exitAngle);
            waypoints.add(waypoint);

            System.out.println("Number of Waypoints: " + waypoints.size());

            this.textArea_waypointsBox.setText(this.textArea_waypointsBox.getText() + '\n'
                    + "X: " + x + "  Y: " + y + "   A: " + exitAngle + " WAYPOINT: " + waypoints.size());
        } catch (NullPointerException | NumberFormatException e) {
            System.out.println("Invalid number input. Try again.");
        }
    }

    private void viewPathActionPerformed(ActionEvent evt) {
        //graphPoints(finalizeTrajectory());
    }

    private void resetActionPerformed(ActionEvent evt) {
        waypoints = new ArrayList<>();
        this.textArea_waypointsBox.setText("RESET\n");
    }

    private void exportActionPerformed(ActionEvent evt) {
        Pathfinder.writeToCSV(new File(new File(path.toUri()), "output.csv"), finalizeTrajectory());
    }

    private void pathNameActionPerformed(ActionEvent evt) {
    }

    private static void writeFile(String path, Trajectory trajectory) {
        File file = new File("output.csv");
        Pathfinder.writeToCSV(file, trajectory);
        System.out.println("File written!");
    }

    public Trajectory finalizeTrajectory() {
        double max_velocity = 1.7, max_acceleration = 2.0, max_jerk = 60.0;
        Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_QUINTIC,
                Trajectory.Config.SAMPLES_HIGH, 0.05, max_velocity, max_acceleration, max_jerk); // Delta time 0.05 = 1/20
        Waypoint[] array = new Waypoint[waypoints.size()];
        return Pathfinder.generate(waypoints.toArray(array), config);
    }
}
