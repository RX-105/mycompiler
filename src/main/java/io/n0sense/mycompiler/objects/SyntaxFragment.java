package io.n0sense.mycompiler.objects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SyntaxFragment {
    public Integer identity;
    public String content;
}
