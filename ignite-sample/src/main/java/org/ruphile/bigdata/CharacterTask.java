/*
import org.apache.ignite.IgniteException;
import org.apache.ignite.compute.ComputeJob;
import org.apache.ignite.compute.ComputeJobAdapter;
import org.apache.ignite.compute.ComputeJobResult;
import org.apache.ignite.compute.ComputeTaskSplitAdapter;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

class CharacterTask extends ComputeTaskSplitAdapter<String, Long> {

    @Override
    protected Collection<? extends ComputeJob> split(int i, String s) throws IgniteException {
        System.out.println("split开始执行");
        Collection<ComputeJob> jobs = new LinkedList<>();

        for (final String word : s.split(" ")) {
            jobs.add(new ComputeJobAdapter() {
                @Override
                public Object execute() {
                    System.out.println();
                    System.out.println(">>> Printing '" + word + "' on this node from ignite job.");

                    // Return number of letters in the word.
                    return word.length();
                }
            });
        }
        System.out.println("split执行完毕");
        return jobs;
    }

    @Override
    public @Nullable Long reduce(List<ComputeJobResult> list) throws IgniteException {
        Long sum = 0L;
        for (ComputeJobResult res : list) {
            System.out.println(">>> Got result: " + res.getData());
            sum += res.<Long>getData();
        }
        return sum;
    }
}
*/
