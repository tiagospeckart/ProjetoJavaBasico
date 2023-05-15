import builders.StudentsBuilder;
import entities.Student;

import java.util.*;

public class Main {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("""
                Input (by item list) desired program:
                1) Students who passed (min grade 7.0)
                2) Students who did not pass
                3) Students with max grade (10)
                4) Student(s) with lowest grade
                5) Top 3 student grades (ties included)
                6) Lowest 3 student grades (ties included)
                7) Average grade of all students (sorted highest to lowest)
                """);
		String option = scanner.nextLine();

		List<Student> allStudents = StudentsBuilder.getAllStudents();

		switch (option) {
			case "1" -> displayStudentsWhoPassed(allStudents);
			case "2" -> displayStudentsWhoDidNotPass(allStudents);
			case "3" -> displayStudentsWithMaxGrade(allStudents);
			case "4" -> displayStudentsWithLowestGrade(allStudents);
			case "5" -> displayTopThreeStudentGrades(allStudents);
			case "6" -> displayLowestThreeStudentGrades(allStudents);
			case "7" -> displayAverageGradeOfAllStudents(allStudents);
			default -> System.out.println("Invalid option.");
		}
		scanner.close();
	}

	private static float getAverageGrade(Student student) {
		float grade1 = student.getTestOne();
		float grade2 = student.getTestTwo();
		float grade3 = student.getTestThree();
		return (grade1 + grade2 + grade3) / 3;
	}

	private static float getLowestGrade(Student student) {
		return Math.min(Math.min(student.getTestOne(), student.getTestTwo()), student.getTestThree());
	}

	private static float getHighestGrade(Student student) {
		return Math.max(Math.max(student.getTestOne(), student.getTestTwo()), student.getTestThree());
	}

	private static void displayStudentsWhoPassed(List<Student> allStudents) {
		for (Student student : allStudents) {
			float meanGrade = getAverageGrade(student);

			if (meanGrade >= 7) {
				System.out.printf("%d - %s : Average = %.1f%n", student.getCode(), student.getName(), meanGrade);
			}
		}
	}

	private static void displayStudentsWhoDidNotPass(List<Student> allStudents) {
		for (Student student : allStudents) {
			float passingGrade = 7;
			float meanGrade = getAverageGrade(student);
			float missingGrade = passingGrade - meanGrade;

			if (meanGrade < 7) {
				System.out.printf("%d - %s : Average = %.1f (Missing = %.1f)%n", student.getCode(), student.getName(), meanGrade, missingGrade);
			}
		}
	}

	private static void displayStudentsWithMaxGrade(List<Student> allStudents) {
		for (Student student : allStudents) {
			float grade1 = student.getTestOne();
			float grade2 = student.getTestTwo();
			float grade3 = student.getTestThree();

			if (grade1 == 10 || grade2 == 10 || grade3 == 10){
				System.out.printf("%d - %s %n", student.getCode(), student.getName());
			}
		}
	}

	private static void displayStudentsWithLowestGrade(List<Student> allStudents) {
		List<Float> minGrades = new ArrayList<>();
		float overallLowestGrade = Float.MAX_VALUE;

		for (Student student : allStudents) {
			float studentLowestGrade = getLowestGrade(student);
			minGrades.add(studentLowestGrade);
			overallLowestGrade = Math.min(overallLowestGrade, studentLowestGrade);
		}

		List<Student> studentsWithLowestGrade = new ArrayList<>();
		for (int i = 0; i < allStudents.size(); i++) {
			if (minGrades.get(i) == overallLowestGrade) {
				studentsWithLowestGrade.add(allStudents.get(i));
			}
		}

		for (Student student : studentsWithLowestGrade) {
			System.out.printf("%d - %s : Grade = %.1f%n", student.getCode(), student.getName(), overallLowestGrade);
		}
	}

	private static void displayTopThreeStudentGrades(List<Student> allStudents) {
		Set<Float> maxGrades = new HashSet<>();
		float overallHighestGrade = Float.MIN_VALUE;

		for (Student student : allStudents) {
			float studentHighestGrade = getHighestGrade(student);
			maxGrades.add(studentHighestGrade);
			overallHighestGrade = Math.max(overallHighestGrade, studentHighestGrade);
		}

		// Arranging the Set of max grades per student
		List<Float> maxGradesList = new ArrayList<>(maxGrades);
		Collections.sort(maxGradesList, Collections.reverseOrder());

		int rank = 1;
		for (float maxGrade : maxGradesList) {
			List<Student> topScorers = new ArrayList<>();
			for (Student student : allStudents) {
				if (student.getTestOne() == maxGrade ||
						student.getTestTwo() == maxGrade ||
						student.getTestThree() == maxGrade) {
					topScorers.add(student);
				}
			}

			System.out.printf("%dº - ", rank);
			for (int j = 0; j < topScorers.size(); j++) {
				System.out.printf("%s : Grade = %.1f", topScorers.get(j).getName(), maxGrade);
				if (j < topScorers.size() - 1) {
					System.out.print("; ");
				} else {
					System.out.printf(";%n");
				}
			}

			rank++;
			if (rank > 3) {
				break;
			}
		}
	}

	private static void displayLowestThreeStudentGrades(List<Student> allStudents) {
		Set<Float> minGrades = new HashSet<>();
		float overallLowestGrade = Float.MAX_VALUE;

		// Getting the lowest grade for each student
		for (Student student : allStudents) {
			float studentLowestGrade = getLowestGrade(student);
			minGrades.add(studentLowestGrade);
			overallLowestGrade = Math.min(overallLowestGrade, studentLowestGrade);
		}

		// Arranging the Set of min grades per student
		List<Float> minGradesList = new ArrayList<>(minGrades);
		Collections.sort(minGradesList);

		int rank = 1;
		for (float minGrade : minGradesList) {
			List<Student> lowestScorers = new ArrayList<>();
			for (Student student : allStudents) {
				if (student.getTestOne() == minGrade ||
						student.getTestTwo() == minGrade ||
						student.getTestThree() == minGrade) {
					lowestScorers.add(student);
				}
			}

			System.out.printf("%dº - ", rank);
			for (int j = 0; j < lowestScorers.size(); j++) {
				System.out.printf("%s : Grade = %.1f", lowestScorers.get(j).getName(), minGrade);
				if (j < lowestScorers.size() - 1) {
					System.out.printf("; ");
				} else {
					System.out.printf(";%n");
				}
			}

			rank++;
			if (rank > 3) {
				break;
			}
		}
	}

	private static void displayAverageGradeOfAllStudents(List<Student> allStudents) {
		List<Object[]> studentAverages = new ArrayList<>();

		// Calculate average grades for all students
		for (Student student : allStudents) {
			float meanGrade = getAverageGrade(student);
			studentAverages.add(new Object[]{student, meanGrade});
		}

		// Sort the list from highest to lowest average grade
		studentAverages.sort((a, b) -> Float.compare((float) b[1], (float) a[1]));

		// Display the data in the specified format
		int position = 1;
		for (Object[] studentAverage : studentAverages) {
			Student student = (Student) studentAverage[0];
			float average = (float) studentAverage[1];
			System.out.printf("%d - %d - %s : Average = %.1f%n", position, student.getCode(), student.getName(), average);
			position++;
		}
	}
}