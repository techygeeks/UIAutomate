package com.core;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

public class Main2 {

	public static void main(String[] args) {
		try {
			Document doc = Jsoup.parse(new File("static/UICode.html"), null);
			Elements allElements = doc.getAllElements();
			for (Element element : allElements) {
				// if element is head skip
				if (/*
					 * element.tagName().equals("#root") ||
					 * element.tagName().equals("html") ||
					 */("head").equals(element.tagName())) {
					Node bodyElement = element.nextElementSibling();
					// get form
					List<Node> allBodyNodes = bodyElement.childNodes();
					for (Node node : allBodyNodes) {
						if ("form".equals(node.nodeName())) {
							for (Node formNode : node.childNodes()) {
								if (formNode.hasAttr("class") && formNode.attr("class").equals("fullPageContent")) {
									for (Node fullPageContentNode : formNode.childNodes()) {
										if (fullPageContentNode.hasAttr("id")
												&& fullPageContentNode.attr("id").equals("PAGE_BODY")) {
											for (Node pageBodyNode : fullPageContentNode.childNodes()) {
												if (pageBodyNode.nodeName().equals("table")
														&& pageBodyNode.hasAttr("class")
														&& pageBodyNode.attr("class").equals("multiBox")) {
													for (Node tableNode : pageBodyNode.childNodes()) {
														if (tableNode.nodeName().equals("tbody")) {
															for (Node tBodyNode : tableNode.childNodes()) {
																if (tBodyNode.nodeName().equals("tr")) {
																	for (Node trNode : tBodyNode.childNodes()) {
																		if (trNode.nodeName().equals("td")) {
																			for (Node tdNode : trNode.childNodes()) {
																				if (tdNode.nodeName().equals("table")) {
																					getPropertiesFromInnerTableNode(
																							tdNode);
																				}
																				// otherwise
																				// fetch
																				// text
																				// directly
																				else {
																					System.out
																							.println(tdNode.toString());
																				}
																			}
																		}
																	}
																}

															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static Map<String, String> getPropertiesFromInnerTableNode(Node tdNode) {
		for (Node tableNodeInner : tdNode.childNodes()) {
			if (tableNodeInner.nodeName().equals("tbody")) {
				for (Node tBodyNodeInner : tableNodeInner.childNodes()) {
					if (tBodyNodeInner.nodeName().equals("tr") && "efTableRow".equals(tBodyNodeInner.attr("class"))) {
						for (Node trNodeInner : tBodyNodeInner.childNodes()) {
							if (trNodeInner.nodeName().equals("td") && "efTableLbl".equals(trNodeInner.attr("class"))) {
								System.out.println("Label is : " + trNodeInner.childNode(0));
							}else if(trNodeInner.nodeName().equals("td") && "efTableData".equals(trNodeInner.attr("class"))){
								//this contains table
								for (Node tdNodeInner : trNodeInner.childNodes()) {
									if (tdNodeInner.nodeName().equals("table")) {
										getPropertiesFromInnerTableNode(tdNodeInner);
									}
									}
							}
						}
					} else if (tBodyNodeInner.nodeName().equals("tr")) {
						for (Node trNodeInner : tBodyNodeInner.childNodes()) {
							if (trNodeInner.nodeName().equals("td")) {
								for (Node tdNodeInner : trNodeInner.childNodes()) {
									if (tdNodeInner.nodeName().equals("table")) {
										getPropertiesFromInnerTableNode(tdNodeInner);
									} else if (tdNodeInner.nodeName().equals("label")) {
										System.out.println("single label : " + tdNodeInner);
									}
									// otherwise fetch text directly
									else if (!tdNodeInner.childNodes().isEmpty()) {
										for (Node otherTypeNode : tdNodeInner.childNodes()) {
											getPropertiesFromInnerTableNode(otherTypeNode);
										}
									} else {
										System.out.println("extra data : "+tdNodeInner.toString());
									}
								}
							}
						}

					}
				}
			} else if (tableNodeInner.nodeName().equals("table")) {
				getPropertiesFromInnerTableNode(tableNodeInner);
			} else {
				processOtherTypesNodes(tableNodeInner);
			}
		}
		return null;

	}

	private static void processOtherTypesNodes(Node tableNodeInner) {
		for (Node otherTypeNode : tableNodeInner.childNodes()) {
			if (otherTypeNode.nodeName().equals("input") || otherTypeNode.nodeName().equals("textarea")) {
				System.out.println("Useful data ---------" + otherTypeNode.toString());
			} else if (otherTypeNode.nodeName().equals("table")) {
				getPropertiesFromInnerTableNode(otherTypeNode);
			} else if (otherTypeNode.nodeName().equals("div") || otherTypeNode.nodeName().equals("span")) {
				processOtherTypesNodes(otherTypeNode);
			} else {
				//System.out.println("other data-------------" + otherTypeNode.toString());
			}
		}

	}

}
