package uk.ac.cam.ch.wwmm.opsin;

import java.util.ArrayList;
import java.util.List;

class Element {

	private String name;
	private String value;
	private Element parent = null;
	private List<Element> children = new ArrayList<Element>();
	private List<Attribute> attributes = new ArrayList<Attribute>();
	
	Element(String name) {
		this.name = name;
		this.value = "";
	}

	/**
	 * Creates a deep copy with no parent
	 * @param element
	 */
	Element(Element element) {
		this.name = element.name;
		this.value = element.value;
		children = new ArrayList<Element>();
		for (Element childEl : element.children) {
			Element newChild = new Element(childEl);
			newChild.setParent(this);
			children.add(newChild);
		}
		
		attributes = new ArrayList<Attribute>();
		for (Attribute atr : element.attributes) {
			attributes.add(new Attribute(atr));
		}
	}

	/**
	 * Gets child elements with this name (in iteration order)
	 * @param name
	 * @return
	 */
	List<Element> getChildElements(String name) {
		List<Element> elements = new ArrayList<Element>();
		for (Element element : children) {
			if (element.name.equals(name)) {
				elements.add(element);
			}
		}
		return elements;
	}

	/**
	 * Returns a copy of the child elements
	 * 
	 * @return
	 */
	List<Element> getChildElements() {
		return new ArrayList<Element>(children);
	}

	/**
	 * Returns the first child element with the specified name
	 * 
	 * @param name
	 * @return
	 */
	Element getFirstChildElement(String name) {
		for (Element child : children) {
			if (child.getName().equals(name)) {
				return child;
			}
		}
		return null;
	}

	Element getChild(int position) {
		return children.get(position);
	}

	int getChildCount() {
		return children.size();
	}

	void addAttribute(Attribute attribute) {
		attributes.add(attribute);
	}
	
	void addAttribute(String atrName, String atrValue) {
		attributes.add(new Attribute(atrName, atrValue));
	}

	boolean removeAttribute(Attribute attribute) {
		return attributes.remove(attribute);
	}

	/**
	 * Returns the attribute with the given name
	 * or null if the attribute doesn't exist
	 * @param name
	 * @return
	 */
	Attribute getAttribute(String name) {
		for (Attribute a : attributes) {
			if (a.getName().equals(name)) {
				return a;
			}
		}
		return null;
	}

	/**
	 * Returns the value of the attribute with the given name
	 * or null if the attribute doesn't exist
	 * @param name
	 * @return
	 */
	String getAttributeValue(String name) {
		Attribute attribute = getAttribute(name);
		if (attribute != null) {
			return attribute.getValue();
		}
		return null;
	}

	int getAttributeCount() {
		return attributes.size();
	}

	Attribute getAttribute(int index) {
		return attributes.get(index);
	}

	String getName() {
		return name;
	}

	void setName(String name) {
		this.name = name;
	}

	void setValue(String text) {
		this.value = text;
	}

	String toXML() {
		return toXML(0).toString();
	}
	
	private StringBuilder toXML(int indent) {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < indent; i++) {
			result.append("  ");
		}
		result.append('<');
		result.append(name);
		for (Attribute atr : attributes) {
			result.append(' ');
			result.append(atr.toXML());
		}
		result.append('>');
		if (children.size() > 0){
			for (Element child : children) {
				result.append('\n');
				result.append(child.toXML(indent + 1));
			}
			result.append('\n');
			for (int i = 0; i < indent; i++) {
				result.append("  ");
			}
			result.append("</");
			result.append(name);
			result.append('>');
		}
		else{
			result.append(value);
			result.append("</");
			result.append(name);
			result.append('>');
		}

		return result;
	}

	String getValue() {
		return value;
	}

	Element getParent() {
		return this.parent;
	}

	void detach() {
		if (parent != null) {
			parent.removeChild(this);
		}
	}

	void insertChild(Element child, int position) {
		child.setParent(this);
		children.add(position, child);
	}

	void appendChild(Element child) {
		child.setParent(this);
		children.add(child);
	}

	int indexOf(Element child) {
		return children.indexOf(child);
	}
	
	boolean removeChild(Element child) {
		child.setParent(null);
		return children.remove(child);
	}

	Element removeChild(int i) {
		Element removed = children.remove(i);
		removed.setParent(null);
		return removed;
	}

	void replaceChild(Element oldChild, Element newChild) {
		int position = indexOf(oldChild);
		if (position == -1) {
			throw new RuntimeException("oldChild is not a child of this element.");
		}
		removeChild(position);
		insertChild(newChild, position);
	}
	
	private void setParent(Element newParentEl) {
		this.parent = newParentEl;
	}
	
	public String toString() {
		return toXML();
	}

}
