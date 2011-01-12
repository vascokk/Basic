package org.basic.google.codejam.practice;

import org.basic.datastrutcures.Pair;
import org.basic.datastrutcures.graphs.Edge;
import org.basic.datastrutcures.graphs.Graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Sets.newHashSet;

public class ProblemCSolver {
    private final int mod = 9901;

    public static int solve(int n, List<Edge> forbiddenEdges) {
        return new ProblemCSolver(forbiddenEdges).solveImpl(n);
    }

    private final Graph graph = new Graph();
    private final Set<Integer> forbiddenNodes = newHashSet();

    public ProblemCSolver(List<Edge> forbiddenEdges) {
        for (Edge e : forbiddenEdges) {
            graph.add(e);
            forbiddenNodes.add(e.u);
            forbiddenNodes.add(e.v);
        }
    }

    private int solveImpl(int n) {
        // add the first edge arbitrarily
        List<Integer> remainingForbiddenNodes = newArrayList(forbiddenNodes);
        int firstNode = remainingForbiddenNodes.get(0);
        remainingForbiddenNodes = remainingForbiddenNodes.subList(1, remainingForbiddenNodes.size());
        int secondNode = graph.getEndpoints(firstNode).iterator().next();
        remainingForbiddenNodes.remove((Integer) secondNode);

        int freeNodes = n - forbiddenNodes.size();

        // int freeNodesInverse= new BigInteger(Integer.toString(freeNodes)).modInverse(new BigInteger(Integer.toString(mod))).intValue();

        return solveImplUsingEdge(remainingForbiddenNodes, freeNodes, firstNode, secondNode, firstNode);
    }

    private int solveImplUsingEdge(List<Integer> remainingForbiddenNodes, int freeNodes, int previousNode, int nextNode, int lastNode) {
        int sum = 0;
        // try a direct path if allowed
        if (!graph.hasEdge(previousNode, nextNode)) {
            sum += solveImpl(remainingForbiddenNodes, freeNodes, nextNode, lastNode);
            sum %= mod;
        }

        // try non-direct paths
        for (int j = 1; j <= freeNodes; ++j) {
            int numberOfPaths = getNumberOfPaths(j, freeNodes);
            sum += numberOfPaths * solveImpl(remainingForbiddenNodes, freeNodes - j, nextNode, lastNode);
            sum %= mod;
        }
        return sum;
    }

    private int solveImpl(List<Integer> remainingForbiddenNodes, int freeNodes, int previousNode, int lastNode) {
        if (remainingForbiddenNodes.size() == 0) {
            // return to the last node
            if (freeNodes == 0) {
                // try a direct path if allowed
                if (graph.hasEdge(previousNode, lastNode)) {
                    return 0;
                }
                return 1;
            }
            // can't do a direct route, this will leave a few nodes hanging.
            // we must use all n nodes
            return getNumberOfPaths(freeNodes, freeNodes);
        }

        int sum = 0;
        for (int i = 0; i < remainingForbiddenNodes.size(); ++i) {
            int nextNode = remainingForbiddenNodes.get(i);
            ArrayList<Integer> nextRemaining = newArrayList(remainingForbiddenNodes);
            nextRemaining.remove(i);

            sum += solveImplUsingEdge(nextRemaining, freeNodes, previousNode, nextNode, lastNode);
        }
        return sum;
    }

    /**
     * Returns the number of hamiltonian paths via a graph with n nodes, that use exactly k nodes
     *
     * @param k
     * @param n
     * @return
     */
    private int getNumberOfPaths(int k, int n) {
        if (k <= 0)
            return 0;

        if (k == 1)
            return n;

        Pair<Integer, Integer> p = new Pair<Integer, Integer>(k, n);
        Integer cached = pathCountCache.get(p);
        if (cached != null)
            return cached;

        int result = n * getNumberOfPaths(k - 1, n - 1) % mod;
        pathCountCache.put(p, result);
        return result;
    }

    private final Map<Pair<Integer, Integer>, Integer> pathCountCache = newHashMap();
}
