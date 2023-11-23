package Methods.EPDS;

import Class.EPDS.SecureDistribution;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class CandidateTable {
    private int n; // ϣ�����ֵ�Ԫ������
    private Map<SecureDistribution, Double> candidate;

    public CandidateTable(int n) {
        this.n = n;
        this.candidate = new LinkedHashMap<>();
    }

    public void add(SecureDistribution key, double value) {


        candidate.put(key, value);


        /*if (candidate.size() > n) {

            double minKey = candidate.values().stream().min(Double::compare).orElse(Double.NaN);
            candidate.remove(minKey);

        }*/
    }

    public Map<SecureDistribution,Double> getCandidate() {
        // ���� double ֵ��������
        return candidate.entrySet().stream()
                .sorted(Entry.comparingByValue())
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

}
