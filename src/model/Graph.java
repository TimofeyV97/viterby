package model;

import view.ConsoleView;

import static model.Constants.INF;

public class Graph {

	private final Vertex [] vertexList;

	private final int [][] adjMatrix;

	private int vertexNumber;

	private final ConsoleView consoleView;

	public Graph(final int vertexNum) {
		this.vertexList = new Vertex[vertexNum];
		this.adjMatrix = new int [vertexNum][vertexNum];
		this.vertexNumber = 0;
		this.consoleView = new ConsoleView();

		for (int i = 0; i < vertexNum; i++) {
			for (int j = 0; j < vertexNum; j++) {
				adjMatrix[i][j] = INF;
			}
		}
	}

	public Vertex [] getVertexList() {
		return this.vertexList;
	}

	public int [][] getAdjMatrix() {
		return this.adjMatrix;
	}

	public void addVertex(final String label, final int direction) {
		this.vertexList[vertexNumber++] = new Vertex(label, direction);
	}

	public void addEdge(final int start, final int end, final int weight) {
		this.adjMatrix[start][end] = weight;
	}

	public void printAdjMatrix() {
		this.consoleView.printAdjMatrix(adjMatrix, vertexNumber);
	}

	public static class Vertex {

		private String label;

		private int direction;

		public Vertex(final String label, final int direction) {
			this.label = label;
			this.direction = direction;
		}

		public String getLabel() {
			return this.label;
		}

		public void setLabel(final String label) {
			this.label = label;
		}

		public int getDirection() {
			return this.direction;
		}

		public void setDirection(final int direction) {
			this.direction = direction;
		}

	}

}
