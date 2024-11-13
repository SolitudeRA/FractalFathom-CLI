package org.protogalaxy.fractalfathom.cli.analysis

import org.junit.jupiter.api.BeforeEach
import spoon.Launcher
import spoon.reflect.declaration.CtClass
import java.io.File

open class BaseTest {

    protected lateinit var ctClass: CtClass<*>

    @BeforeEach
    fun setUp() {
        val launcher = Launcher()
        // 添加测试代码文件的路径
        val basePath = "src/test/kotlin/org/protogalaxy/fractalfathom/cli/resources"
        launcher.addInputResource(File("$basePath/UserService.java").absolutePath)
        // 添加注解定义文件
        launcher.addInputResource(File("$basePath/annotations/Feature.java").absolutePath)
        launcher.addInputResource(File("$basePath/annotations/Mapping.java").absolutePath)
        launcher.addInputResource(File("$basePath/annotations/FeatureType.java").absolutePath)
        launcher.addInputResource(File("$basePath/annotations/MappingType.java").absolutePath)
        // 添加依赖的类
        launcher.addInputResource(File("$basePath/UserRepository.java").absolutePath)
        launcher.addInputResource(File("$basePath/User.java").absolutePath)
        launcher.buildModel()

        ctClass = launcher.model.allTypes.first { it.simpleName == "UserService" } as CtClass<*>
    }
}
