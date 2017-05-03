package skhu.cse.network.omni_stadium;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.zip.Inflater;

import pl.polidea.view.ZoomView;

public class ReservActivity extends Fragment {

    @Nullable
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("예매 기능");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.reserv_main, null, false);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ZoomView zoomView = new ZoomView(getContext());
        zoomView.addView(v);
        zoomView.setLayoutParams(layoutParams);
        zoomView.setMiniMapEnabled(true); // 좌측 상단 검은색 미니맵 설정
        zoomView.setMaxZoom(4f); // 줌 Max 배율 설정  1f 로 설정하면 줌 안됩니다.
        zoomView.setMiniMapCaption(""); //미니 맵 내용
        zoomView.setMiniMapCaptionSize(20); // 미니 맵 내용 글씨 크기 설정


        ConstraintLayout layout = (ConstraintLayout)getActivity().findViewById(R.id.container);
        container.addView(zoomView);

        return inflater.inflate(R.layout.reserv_main,container, false);
    }

}
