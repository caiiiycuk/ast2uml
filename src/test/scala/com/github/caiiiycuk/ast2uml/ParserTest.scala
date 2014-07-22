package com.github.caiiiycuk.ast2uml

import org.scalatest._
import com.github.caiiiycuk.ast2uml.clang.ClangAstParser
import com.github.caiiiycuk.ast2uml.query.AstQuery

class ParserTest extends FlatSpec with Matchers {

  behavior of "Parser"

  it should "parse clang output (GEO_LAYER_LIB_CPP)" in {
    implicit val ast = ClangAstParser(ClangOutput.GEO_LAYER_LIB_CPP)
    val classes = AstQuery.findAll(classOf[AstClass]) ::: AstQuery.findAll(classOf[AstStruct])
    val nodes = classes.map(_.map(_.astName))

    nodes should equal(
      List(List(".", "geo_render", "C_GeoLayer"),
        List(".", "geo_render", "S_DrawData"),
        List(".", "geo_render", "S_DrawInfo"),
        List(".", "geo_render", "S_LayerProperty")))
  }

  it should "parse clang output (SDL_APPLICATION_HPP)" in {
    implicit val ast = ClangAstParser(ClangOutput.SDL_APPLICATION_HPP)
    val classes = AstQuery.findAll(classOf[AstClass]) ::: AstQuery.findAll(classOf[AstStruct])
    val nodes = classes.map(_.map(_.astName))

    nodes should equal(
      List(List(".", "geo_render", "C_SDLEvent"), List(".", "geo_render", "C_SDLApplication")))
  }

}