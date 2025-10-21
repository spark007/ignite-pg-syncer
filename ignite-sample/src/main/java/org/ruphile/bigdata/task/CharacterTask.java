package org.ruphile.bigdata.task;

import org.apache.ignite.IgniteException;
import org.apache.ignite.compute.ComputeJob;
import org.apache.ignite.compute.ComputeJobAdapter;
import org.apache.ignite.compute.ComputeJobResult;
import org.apache.ignite.compute.ComputeTaskSplitAdapter;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

public class CharacterTask extends ComputeTaskSplitAdapter<String, Long> {
    Logger logger = Logger.getLogger(CharacterTask.class.getName());

    @Override
    protected Collection<? extends ComputeJob> split(int i, String s) throws IgniteException {
        logger.info("split开始执行");
        Collection<ComputeJob> jobs = new LinkedList<>();

        for (final String word : s.split(" ")) {
            jobs.add(new ComputeJobAdapter() {
                @Override
                public Object execute() {
                    logger.info(">>> Printing '" + word + "' on this node from ignite job.");

                    // Return number of letters in the word.
                    return word.length();
                }
            });
        }
        logger.info("split执行完毕");
        return jobs;
    }

    @Override
    public @Nullable Long reduce(List<ComputeJobResult> list) throws IgniteException {
        int sum = 0;
        for (ComputeJobResult res : list) {
            logger.info(">>> Got result: " + res.<Integer>getData());
            sum += res.<Integer>getData();
        }
        return (long) sum;
    }
}
