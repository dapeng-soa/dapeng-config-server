package com.github.dapeng.socket;

import java.util.Arrays;
import java.util.List;

/**
 * @author Shadow
 * @date
 */
public interface SystemParas {

    List<String> BUILDSERVERCOMMANDS = Arrays.asList(AllowedCopmmand.BRANCH.name().toLowerCase(), AllowedCopmmand.BUILD.name().toLowerCase(), AllowedCopmmand.IMAGES.name().toLowerCase());

    String SHELLNAME = "sh /Users/struy/project/today/dapeng-config-server/src/main/resources/shell/agent.sh ";

    String COMMAS=";";

    enum AllowedCopmmand{
        BRANCH,
        BUILD,
        DEPLOY,
        ROLLBACK,
        IMAGES,
        SERVICES,
        SERVICERESTART,
        SERVICESTOP
    }

    public static void main(String[] args) {
        System.out.println(AllowedCopmmand.BRANCH);
    }
}
