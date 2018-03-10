package postman;

import java.util.*;

/**
 * 10. Listonosz
 */

public class Postman {

    private Graph graph;
    private Graph hGraph;
    private Set<Integer> visitedVertices;
    private double exactTime;
    private double heuristicTime;

    Postman(int n) {

        graph = new Graph(n);
        hGraph = new Graph(n);
        visitedVertices = new HashSet<>();
    }

    Postman(List<Integer> list) {
        this(list.get(0));
        makeGraphFromFile(list);
    }

    private void makeGraphFromFile(List<Integer> list) {
        for (int i = 1; i < list.size() - 1; i += 2)
            addEdge(list.get(i), list.get(i + 1));
    }

    public void createGraph(boolean adjacencyMatrix[][]) {
        for (int i = 0; i < graph.getNumOfVertices(); ++i) {
            for (int j = i + 1; j < graph.getNumOfVertices(); ++j) {
                if (adjacencyMatrix[i][j])
                    addEdge(i, j);
            }
        }
    }

    private void addEdge(int v, int u) {
        graph.addEdge(v, u);
        hGraph.addEdge(v, u);
    }

    public double getExactTime() {
        return exactTime;
    }

    public double getHeuristicTime() {
        return heuristicTime;
    }

    public int getNumOfVertices() {
        return graph.getNumOfVertices();
    }

    public int getNumOfEdges() {
        return graph.getNumOfEdges();
    }

    public boolean isConnected() {

        if (graph.getNumOfVertices() > 0) {

            clearVisitedVertices();
            DFS(0);
            return graph.getNumOfVertices() == visitedVertices.size();
        }
        return false;
    }

    private void clearVisitedVertices() {

        if (!visitedVertices.isEmpty())
            visitedVertices.clear();
    }

    private void DFS(int v) {

        visitedVertices.add(v);

        for (Integer u : graph.getAdjacencyList()[v]) {
            if (!visitedVertices.contains(u))
                DFS(u);
        }
    }

    public LinkedList<Integer> getExactCycle(int beginVertice) {
        long tStart = System.currentTimeMillis();

        duplicateEdges(findMinimumPerfectMatching(getOddDegreeVertices()));
        LinkedList<Integer> cycleList = new LinkedList<>();
        DFSEuler(beginVertice, cycleList);

        long tEnd = System.currentTimeMillis();
        long tDelta = tEnd - tStart;
        exactTime = tDelta / 1000.0;
        return cycleList;
    }

    private void duplicateEdges(LinkedList<Integer>[] list) {

        if (list == null)
            return;

        Iterator<Integer> iterator;
        for (LinkedList<Integer> aList : list) {

            iterator = aList.listIterator();
            int v = iterator.next();
            int u;

            while (iterator.hasNext()) {

                u = iterator.next();
                graph.addEdge(v, u);
                v = u;
            }
        }
    }

    private LinkedList<Integer>[] findMinimumPerfectMatching(LinkedList<Integer> oddVertices) {

        if (oddVertices.size() < 2)
            return null;

        int n = oddVertices.size() / 2;

        @SuppressWarnings("unchecked")
        LinkedList<Integer> matchingList[] = new LinkedList[n];
        LinkedList<Integer> list;

        for (int i = 0; i < n; ++i) {

            int index = 0;
            int removeIndex;
            Iterator<Integer> iterator = oddVertices.listIterator();
            Integer v = iterator.next();
            matchingList[i] = findShortestPathBFS(v, iterator.next());
            removeIndex = ++index;

            while (iterator.hasNext()) {

                Integer next = iterator.next();
                list = findShortestPathBFS(v, next);
                index++;

                if (list.size() < matchingList[i].size()) {
                    matchingList[i] = list;
                    removeIndex = index;
                }
            }
            oddVertices.remove(removeIndex);
            oddVertices.removeFirst();
        }

        return matchingList;
    }

    private LinkedList<Integer> findShortestPathBFS(int src, int dst) {

        Queue<Integer> queue = new LinkedList<>();
        Map<Integer, Integer> parentVertices = new HashMap<>();
        LinkedList<Integer> shortestPath = new LinkedList<>();
        LinkedList<Integer> list;

        clearVisitedVertices();
        queue.add(src);

        int nextVertice;

        while (!queue.isEmpty()) {

            nextVertice = queue.peek();
            visitedVertices.add(nextVertice);

            list = graph.getAdjacencyList()[nextVertice];
            Integer unvisitedVertice = getUnvisitedVertice(list);

            if (unvisitedVertice != null) {

                queue.add(unvisitedVertice);
                visitedVertices.add(unvisitedVertice);
                parentVertices.put(unvisitedVertice, nextVertice);

                if (unvisitedVertice == dst)
                    break;

            } else {
                queue.poll();
            }
        }

        Integer v = dst;
        while (v != null) {

            shortestPath.addFirst(v);
            v = parentVertices.get(v);
        }

        return shortestPath;
    }

    private Integer getUnvisitedVertice(LinkedList<Integer> list) {
        for (int i : list) {
            if (!visitedVertices.contains(i))
                return i;
        }
        return null;
    }

    private LinkedList<Integer> getOddDegreeVertices() {
        LinkedList<Integer> list = new LinkedList<>();

        for (int i = 0; i < graph.getNumOfVertices(); ++i) {
            if (graph.getAdjacencyList()[i].size() % 2 == 1)
                list.add(i);
        }
        return list;
    }

    private void DFSEuler(int v, LinkedList<Integer> cycleList) {
        LinkedList<Integer> list = graph.getAdjacencyList()[v];
        Integer k = v;
        Iterator<Integer> it = list.listIterator();

        while (it.hasNext()) {

            Integer j;
            j = it.next();

            while (list.contains(j)) {

                list.remove(j);
                graph.getAdjacencyList()[j].remove(k);
                it = list.listIterator();
                DFSEuler(j, cycleList);
            }
        }

        cycleList.addFirst(k);
    }

    public LinkedList<Integer> getHeuristicCycle(int beginVertice) {
        long tStart = System.currentTimeMillis();
        LinkedList<Integer> cycleList = new LinkedList<>();
        Integer[] previousVertices = new Integer[hGraph.getNumOfVertices()];
        ArrayList<Integer> startVertices = new ArrayList<>();
        Integer start = beginVertice;
        Integer current = beginVertice;
        Integer tmp;
        int index = 0;
        boolean flag = true;

        visitedVertices.clear();
        visitedVertices.add(current);
        startVertices.add(start);

        while (true) {

            if (flag)
                cycleList.add(index, current);

            index++;
            flag = true;
            tmp = checkNeighbours(current);

            if (tmp != null) {

                if (previousVertices[tmp] == null || current.equals(start))
                    previousVertices[tmp] = current;

                visitedVertices.add(tmp);
                hGraph.getAdjacencyList()[current].remove(tmp);
                hGraph.getAdjacencyList()[tmp].remove(current);
                current = tmp;

            } else {

                assert current != null;
                if (current.equals(start)) {

                    if (!hasUnvisitedEdges())
                        break;

                    else {
                        current = getVerticeWithUnvisitedEdge(cycleList);
                        start = current;
                        startVertices.add(start);
                        index = cycleList.indexOf(current);
                        flag = false;
                    }

                } else {

                    if (!checkLoop(start, current, previousVertices))
                        current = previousVertices[current];

                    else {
                        LinkedList<Integer> loop = getLoop(current, previousVertices);
                        LinkedList<Integer> part = leaveLoop(cycleList, loop, startVertices);
                        cycleList.addAll(index, part);
                        current = start;
                    }
                }
            }
        }

        long tEnd = System.currentTimeMillis();
        long tDelta = tEnd - tStart;
        heuristicTime = tDelta / 1000.0;
        return cycleList;
    }

    private Integer checkNeighbours(Integer v) {
        if (v == null)
            return null;

        LinkedList<Integer> list = hGraph.getAdjacencyList()[v];
        if (!list.isEmpty()) {
            Iterator<Integer> it = list.listIterator();
            Integer value = it.next();
            while (visitedVertices.contains(value) && it.hasNext())
                value = it.next();
            return value;
        } else
            return null;
    }

    private boolean hasUnvisitedEdges() {
        for (int i = 0; i < hGraph.getNumOfVertices(); ++i) {
            if (!hGraph.getAdjacencyList()[i].isEmpty())
                return true;
        }
        return false;
    }

    private Integer getVerticeWithUnvisitedEdge(LinkedList<Integer> list) {
        Iterator<Integer> it = list.listIterator();
        Integer value;
        while (it.hasNext()) {
            value = it.next();
            if (!hGraph.getAdjacencyList()[value].isEmpty()) {
                return value;
            }
        }
        return null;
    }

    private boolean checkLoop(Integer start, Integer current, Integer[] prev) {
        Integer pom = current;
        for (int i = 0; i < getNumOfVertices(); ++i) {
            pom = prev[pom];
            if (pom == null || pom.equals(start))
                return false;
            else if (pom.equals(current))
                return true;
        }
        return false;
    }

    private LinkedList<Integer> getLoop(Integer current, Integer[] prev) {
        LinkedList<Integer> list = new LinkedList<>();
        Integer pom = current;
        while (true) {
            list.add(pom);
            pom = prev[pom];
            if (pom.equals(current))
                break;
        }
        return list;
    }

    private LinkedList<Integer> leaveLoop(LinkedList<Integer> cycleList, LinkedList<Integer> loop,
                                          ArrayList<Integer> startVertices) {
        LinkedList<Integer> partList = new LinkedList<>();
        int posInLoop = 0;
        int posInCycle = 0;
        int posOfStartVertice = cycleList.indexOf(startVertices.get(startVertices.size() - 1));
        boolean flag = false;

        for (int i = 0; i < loop.size(); ++i) {
            if (startVertices.contains(loop.get(i))) {
                if (i > loop.size() / 2)
                    flag = true;

                posInLoop = i;
                break;
            }
        }

        for (int i = posOfStartVertice; i >= 0; --i) {
            if (cycleList.get(i).equals(loop.get(posInLoop))) {
                posInCycle = i;
                break;
            }
        }

        if (!flag) {
            for (int i = 1; i <= posInLoop; ++i)
                partList.add(loop.get(i));
        } else {
            for (int i = loop.size() - 1; i >= posInLoop; --i)
                partList.add(loop.get(i));
        }

        for (int i = posInCycle; i < posOfStartVertice; ++i)
            partList.add(cycleList.get(i));

        return partList;
    }
}
