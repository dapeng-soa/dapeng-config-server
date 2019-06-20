package com.github.dapeng.k8s.yaml;

import java.io.FileNotFoundException;

/**
 * @author huyj
 * @Created 2019-06-04 17:14
 */
public class YamlTest {


    public static void main(String[] arg0) throws FileNotFoundException {

      /*  String yamlStr = "key: hello yaml";
        Yaml yaml = new Yaml();
        Object ret = yaml.load(yamlStr);
        System.out.println(ret);*/
        String k8sYaml = YamlParseUtils.buildK8sYamlByFile("E:\\workspace\\test\\src\\main\\java\\com\\today\\yaml\\yamlDir\\adminService.yml");
        //System.out.println(k8sYaml);
    }
}
