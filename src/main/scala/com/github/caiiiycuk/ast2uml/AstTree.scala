package com.github.caiiiycuk.ast2uml

abstract class AstNodeSpec
case class AstAccessSpec(level: String) extends AstNodeSpec

case class AstNode(name: String, specs: Seq[AstNodeSpec]) {
  override def toString = name
}

abstract class Ast(node: AstNode, childs: Seq[Ast]) {

  def astChilds = childs

  def astName = node.name

  protected def toString(paddingChar: Char = '~', padding: String = ""): String = {
    val childString = childs.map(_.toString(paddingChar, padding + paddingChar)).mkString
    val astClass = s"${padding}(${getClass.getSimpleName})"
    val postfix = " " * (20 - astClass.length)
    s"${astClass}${postfix}${astName}\n${childString}"
  }

  override def toString() = toString('~')
}

case class AstRoot(childs: List[Ast]) extends Ast(AstNode(".", Seq()), childs)

case class AstNamespace(node: AstNode, childs: List[Ast]) extends Ast(node, childs)

abstract class AstRecord(node: AstNode, childs: List[Ast]) extends Ast(node, childs) {
  def toStruct = AstClass(node, childs)
  def toClass = AstClass(node, childs)
}

case class AstClass(node: AstNode, childs: List[Ast]) extends AstRecord(node, childs)
case class AstStruct(node: AstNode, childs: List[Ast]) extends AstRecord(node, childs)
case class AstUnknown(node: AstNode, childs: List[Ast]) extends Ast(node, childs)

case class AstField(node: AstNode, `type`: Option[String], childs: List[Ast]) extends Ast(node, childs) {
  override def astName = node.specs.headOption match {
    case Some(AstAccessSpec("public")) =>
      s"+${`type`.getOrElse("???")} ${node}"
    case Some(AstAccessSpec("protected")) =>
      s"#${`type`.getOrElse("???")} ${node}"
    case _ =>
      s"-${`type`.getOrElse("???")} ${node}"
  }

}

case class AstMethod(node: AstNode, signature: Option[String], childs: List[Ast]) extends Ast(node, childs) {
  override def astName = node.specs.headOption match {
    case Some(AstAccessSpec("public")) =>
      s"+${node}(${signature.getOrElse("???")})"
    case Some(AstAccessSpec("protected")) =>
      s"#${node}(${signature.getOrElse("???")})"
    case _ =>
      s"-${node}(${signature.getOrElse("???")})"
  }
}