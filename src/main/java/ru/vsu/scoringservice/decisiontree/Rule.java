package ru.vsu.scoringservice.decisiontree;

public record Rule(String attribute, Predicate predicate, Object sampleValue) {

	public boolean match(Item item) {
		Object otherField = item.getFieldValue(this.attribute);
		return this.predicate.eval(otherField, this.sampleValue);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rule other = (Rule) obj;
		if (attribute == null) {
			if (other.attribute != null)
				return false;
		} else if (!attribute.equals(other.attribute))
			return false;
		if (predicate != other.predicate)
			return false;
		if (sampleValue == null) {
			return other.sampleValue == null;
		} else return sampleValue.equals(other.sampleValue);
	}
}
