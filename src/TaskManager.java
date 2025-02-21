import java.util.*;

class Employee {
    String name;
    int skill;
    boolean available;

    Employee(String name, int skill) {
        this.name = name;
        this.skill = skill;
        this.available = true;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Employee employee = (Employee) obj;
        return skill == employee.skill && name.equals(employee.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, skill);
    }
}

class Task {
    String title;
    int difficulty;
    int priority;

    Task(String title, int difficulty, int priority) {
        this.title = title;
        this.difficulty = difficulty;
        this.priority = priority;
    }
}

class SkillBasedManager {
    private final List<Employee> employees = new ArrayList<>();
    private final PriorityQueue<Task> tasks = new PriorityQueue<>(Comparator.comparingInt(t -> t.priority));
    private final Map<Employee, List<Task>> assignedTasks = new HashMap<>();

    public void addEmployee(String name, int skill) {
        Employee emp = new Employee(name, skill);
        employees.add(emp); // to add employee in the list
        assignedTasks.put(emp, new ArrayList<>()); // task list of that employee
    }

    public void addTask(String title, int difficulty, int priority) {
        tasks.offer(new Task(title, difficulty, priority)); // queue sorted by priority
    }

    public void assignTasks() {
        while (!tasks.isEmpty()) {
            Task task = tasks.poll(); // highest priority task
            Employee bestFit = null;

            for (Employee emp : employees) {
                if (emp.available && emp.skill >= task.difficulty) {
                    if (bestFit == null || emp.skill < bestFit.skill) {
                        bestFit = emp;
                    }
                }
            }

            if (bestFit != null) {
                bestFit.available = false;
                assignedTasks.get(bestFit).add(task);  // to store task in employee task list
                System.out.println("Task '" + task.title + "' assigned to " + bestFit.name);
            } else {
                System.out.println("No available employee for task: " + task.title);
            }
        }
    }

    public void showAssignments() {
        System.out.println("\nTask Assignments:");
        for (Map.Entry<Employee, List<Task>> entry : assignedTasks.entrySet()) {
            System.out.print(entry.getKey().name + " (Skill " + entry.getKey().skill + ") - ");
            if (entry.getValue().isEmpty()) {
                System.out.println("No tasks assigned.");
            } else {
                for (Task task : entry.getValue()) {
                    System.out.print("[ " + task.title + " ] ");
                }
                System.out.println();
            }
        }
    }
}

public class TaskManager {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SkillBasedManager manager = new SkillBasedManager();

        // adding employees 
        System.out.print("Enter the number of employees: ");
        int numEmployees = scanner.nextInt();
        scanner.nextLine(); 

        for (int i = 0; i < numEmployees; i++) {
            System.out.print("Enter the Employee Name: ");
            String name = scanner.nextLine();
            System.out.print("Enter the Skill Level (1-10): ");
            int skill = scanner.nextInt();
            scanner.nextLine(); 
            manager.addEmployee(name, skill);
        }

        // adding tasks dynamically
        System.out.print("\nEnter the number of tasks: ");
        int numTasks = scanner.nextInt();
        scanner.nextLine(); 

        for (int i = 0; i < numTasks; i++) {
            System.out.print("Enter Task Title: ");
            String title = scanner.nextLine();
            System.out.print("Enter Task Difficulty (1-10): ");
            int difficulty = scanner.nextInt();
            System.out.print("Enter Task Priority (1-10): ");
            int priority = scanner.nextInt();
            scanner.nextLine(); 
            manager.addTask(title, difficulty, priority);
        }

        // assigning tasks
        System.out.println("\nAssigning Tasks...");
        manager.assignTasks();

        // displaying task assignments
        manager.showAssignments();

        scanner.close();
    }
}
