package com.github.caiiiycuk.ast2uml.clang

import scala.annotation.tailrec
import scala.collection.immutable.Stream
import com.github.caiiiycuk.ast2uml._

object ClangAstParser {

  def apply(lines: Stream[String]): Ast = AstRoot(extractChilds(lines)._2)

  private final val CLASS = """CXXRecordDecl.*class\s(.*)\sdefinition""".r
  private final val STRUCT = """CXXRecordDecl.*struct\s(.*)\sdefinition""".r
  private final val NAMESPACE = """NamespaceDecl.*\s(.*)$""".r
  private final val FIELD = """FieldDecl.*\s(.*)\s'(.*)'(| invalid)$""".r
  private final val METHOD = """CXXMethodDecl.*\s(.*)\s'(.*)'""".r
  private final val ACCESS = """AccessSpecDecl.*\s(private|public|protected)""".r

  private def extractChilds(lines: Stream[String], childs: List[Ast] = List(), specs: List[AstNodeSpec] = List(), nesting: Int = 0): (Stream[String], List[Ast]) = if (lines.isEmpty) {
    (Stream.empty[String], childs)
  } else if (lineNesting(lines.head) - nesting >= 0) {
    val astSpecs = toSpec(lines.head).map(spec => spec :: specs).getOrElse(specs)
    val (astRest, astChilds) = extractChilds(lines.tail, List(), astSpecs, nesting + 1)
    val ast = toAst("c(" + lineNesting(lines.head) + ")" + lines.head, astChilds, astSpecs)
    extractChilds(astRest, childs :+ ast, astSpecs, nesting)
  } else {
    (lines, childs)
  }

  private def lineNesting(line: String) =
    Math.max(Math.max(line.indexOf("|-"), line.indexOf("`-")) / 2, 0)

  private def toSpec(line: String): Option[AstNodeSpec] = {
    val specs = List[Option[AstNodeSpec]](
      ACCESS.findFirstMatchIn(line).map(m => {
        AstAccessSpec(m.group(1))
      }),
      STRUCT.findFirstMatchIn(line).map(m => {
        AstAccessSpec("public")
      }),
      CLASS.findFirstMatchIn(line).map(m => {
        AstAccessSpec("private")
      })).flatten
      
    specs.headOption
  }

  private def toAst(line: String, childs: List[Ast], specs: List[AstNodeSpec]): Ast = {
    val node = List[Option[Ast]](
      CLASS.findFirstMatchIn(line).map(m => {
        AstClass(AstNode(m.group(1), specs), childs)
      }),
      STRUCT.findFirstMatchIn(line).map(m => {
        AstStruct(AstNode(m.group(1), specs), childs)
      }),
      NAMESPACE.findFirstMatchIn(line).map(m => {
        AstNamespace(AstNode(m.group(1), specs), childs)
      }),
      FIELD.findFirstMatchIn(line).map(m => {
        AstField(AstNode(m.group(1), specs), Some(m.group(2).replaceAll("'", "")), childs)
      }),
      METHOD.findFirstMatchIn(line).map(m => {
        AstMethod(AstNode(m.group(1), specs), Some(m.group(2).replaceAll("'", "")), childs)
      }))

    node.flatten.headOption.getOrElse(AstUnknown(AstNode(line, specs), childs))
  }

}