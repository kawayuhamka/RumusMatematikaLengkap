package edlib.rumusmatematikalengkap.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import br.tiagohm.markdownview.MarkdownView;
import br.tiagohm.markdownview.css.ExternalStyleSheet;
import edlib.rumusmatematikalengkap.R;

public class ItemHolder extends RecyclerView.ViewHolder {
    public View view;

    public ItemHolder(View itemView) {
        super(itemView);
        view = itemView;
    }

    public void setTitle(String title) {
        TextView judulKonten = view.findViewById(R.id.judul_konten);
        judulKonten.setText(title);
    }

    public void setImg(Context context, String img) {
        ImageView imgIcon = view.findViewById(R.id.icon_konten);
        Glide.with(context)
                .load(img)
                .into(imgIcon);
    }

    public void setBody(String body) {
        MarkdownView markdownView = view.findViewById(R.id.markdown_content);
        markdownView.addStyleSheet(ExternalStyleSheet.fromAsset("style.css", null));
        markdownView.loadMarkdown(body);
    }
}
