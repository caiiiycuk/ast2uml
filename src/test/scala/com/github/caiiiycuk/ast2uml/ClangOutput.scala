package com.github.caiiiycuk.ast2uml

/**
 * clang -cc1 -ast-dump file
 */
object ClangOutput {
  val GEO_LAYER_LIB_CPP = io.Source.fromFile("src/test/resources/GEO_LAYER_LIB_CPP.clang").mkString.split("\n").toStream
  val RENDER_FACTORY_LIB_CPP = io.Source.fromFile("src/test/resources/RENDER_FACTORY_LIB_CPP.clang").mkString.split("\n").toStream
  val SDL_APPLICATION_HPP = io.Source.fromFile("src/test/resources/SDL_APPLICATION_HPP.clang").mkString.split("\n").toStream
}