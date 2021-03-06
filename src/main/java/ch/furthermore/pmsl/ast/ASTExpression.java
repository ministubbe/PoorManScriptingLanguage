package ch.furthermore.pmsl.ast;

import java.util.ArrayList;

import ch.furthermore.pmsl.Context;
import ch.furthermore.pmsl.ScannerToken;
import ch.furthermore.pmsl.ScannerTokenType;

public class ASTExpression extends AbstractASTNode {
	private final ArrayList<ASTTerm> terms = new ArrayList<ASTTerm>();
	private final ArrayList<ScannerTokenType> combiners = new ArrayList<ScannerTokenType>();
	
	public ASTExpression(ScannerToken t) {
		super(t);
	}
	
	public void add(ASTTerm term) {
		terms.add(term);
	}

	public void add(ScannerTokenType plusMinus) {
		combiners.add(plusMinus);
	}

	@Override
	public void printTransform(StringBuilder sb, Transformer transformer) {
		transformer.transform(this).printTransformInt(sb, transformer);
	}
	
	@Override
	public void printTransformInt(StringBuilder sb, Transformer transformer) {
		for (int i = 0; i < terms.size(); i++) {
			if (i > 0) {
				switch (combiners.get(i - 1)) {
				case PLUS:
					sb.append('+');
					break;
				case MINUS:
					sb.append('-');
					break;
				default: throw new IllegalStateException();
				}
			}
			transformer.transform(terms.get(i)).printTransformInt(sb, transformer);
		}
	}

	public void generate(Context ctx, StringBuilder sb) {
		if (terms.size() > 1) {
			sb.append('(');
		}
		for (int i = 0; i < terms.size(); i++) {
			if (i > 0) {
				switch (combiners.get(i - 1)) {
				case PLUS:
					sb.append('+');
					break;
				case MINUS:
					sb.append('-');
					break;
				default: throw new IllegalStateException();
				}
			}
			terms.get(i).generate(ctx, sb);
		}
		if (terms.size() > 1) {
			sb.append(')');
		}
	}
}
